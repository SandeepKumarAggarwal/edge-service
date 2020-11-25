package com.edge.service.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edge.service.models.AccessControl;
import com.edge.service.models.AccessTracking;
import com.edge.service.repository.AccessControlRepository;
import com.edge.service.repository.AccessTrackingRepository;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Date;

public class AccessFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Value("${ratelimit.default}")
    private int defaultRateLimit;

    @Value("${ratelimit.time}")
    private int defaultTime;

    @Autowired
    AccessControlRepository accessControlRepository;

    @Autowired
    AccessTrackingRepository accessTrackingRepository;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    /**
     * Pre filter to filter out requests if rate limit exceeds either default rate limits OR client opted rate limits
     *
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        String clientID = request.getHeader("clientID");
        String[] subStr = request.getRequestURL().toString().split("/");
        String api = subStr[3];

        if(isRateLimitExceeded(clientID, api)) {
            try {
                response.sendError(429, "too many requests in a given amount of time (rate limiting)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Validate if rate limit exceeded for the given user
     * If not, redirect to the API
     * If yes, throw HTTP Status Code as 429 with error as "TO MANY ATTEMPTS"
     *
     * @param client
     * @param api
     * @return
     */
    private boolean isRateLimitExceeded(String client, String api) {
        AccessControl accessControl = accessControlRepository.findByClientAndApi(client, api);
        int rateLimit = getRateLimit(accessControl, defaultRateLimit);
        int rateLimitMinutes = getRateLimitMinutes(accessControl, defaultTime);
        AccessTracking accessTracking = accessTrackingRepository.findByClientAndApi(client, api);

        if (accessTracking == null ) {
            AccessTracking accessTrackingEntry = getAccessTracking(client, api);
            accessTrackingRepository.save(accessTrackingEntry);
        }
        else if(accessTracking != null && accessTracking.getAccessCount() >= rateLimit && new Date().getTime() - accessTracking.getInitialAccessDate().getTime() <= rateLimitMinutes*60*1000 ) {
            return true;
        } else if(accessTracking != null && accessTracking.getAccessCount() >= rateLimit && new Date().getTime() - accessTracking.getInitialAccessDate().getTime() > rateLimitMinutes*60*1000 ) {
            accessTracking.setAccessCount(1);
            accessTracking.setInitialAccessDate(new Date());
            accessTrackingRepository.save(accessTracking);
        } else {
            accessTracking.setAccessCount(accessTracking.getAccessCount() + 1);
            accessTrackingRepository.save(accessTracking);
        }

        return false;
    }


    /**
     * Get EITHER Default Rate Limit Time OR Opted Limit Time
     *
     * @param accessControl
     * @param defaultTime
     * @return
     */
    private int getRateLimitMinutes(AccessControl accessControl, int defaultTime) {
        int rateLimitMinutes = 0;
        if (accessControl == null) {
            rateLimitMinutes = defaultTime;
        } else {
            rateLimitMinutes = accessControl.getRateLimitMinutes();
        }
        return rateLimitMinutes;
    }

    /**
     * Get EITHER Default Rate Limit  OR Opted Limit
     *
     * @param accessControl
     * @param defaultRateLimit
     * @return
     */
    private int getRateLimit(AccessControl accessControl, int defaultRateLimit) {
        int rateLimit = 0;
        if (accessControl == null) {
            rateLimit = defaultRateLimit;
        } else {
            rateLimit = accessControl.getRateLimit();
        }
        return rateLimit;
    }

    /**
     * Get First Time Access Tracking Creation
     *
     * @param client
     * @param api
     * @return
     */
    private AccessTracking getAccessTracking(String client, String api) {
        AccessTracking accessTrackingEntry = new AccessTracking();
        accessTrackingEntry.setClient(client);
        accessTrackingEntry.setApi(api);
        accessTrackingEntry.setAccessCount(1);
        accessTrackingEntry.setInitialAccessDate(new Date());
        return accessTrackingEntry;
    }


}
