package org.ekstep.interceptor;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ekstep.common.dto.ExecutionContext;
import org.ekstep.common.dto.HeaderParam;
import org.ekstep.common.util.RequestWrapper;
import org.ekstep.common.util.ResponseWrapper;
import org.ekstep.common.util.TelemetryAccessEventUtil;
import org.ekstep.telemetry.TelemetryGenerator;
import org.ekstep.telemetry.TelemetryParams;
import org.ekstep.telemetry.logger.TelemetryManager;

public class ResponseFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		TelemetryGenerator.setComponent("learning-service");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String requestId = getUUID();
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		ExecutionContext.setRequestId(requestId);
		boolean isMultipart = (httpRequest.getHeader("content-type") != null
				&& httpRequest.getHeader("content-type").indexOf("multipart/form-data") != -1);
		String consumerId = httpRequest.getHeader("X-Consumer-ID");
		String channelId = httpRequest.getHeader("X-Channel-Id");
		String appId = httpRequest.getHeader("X-App-Id");
		String path = httpRequest.getRequestURI();

		if (StringUtils.isNotBlank(consumerId))
			ExecutionContext.getCurrent().getGlobalContext().put(HeaderParam.CONSUMER_ID.name(), consumerId);
		TelemetryManager.info("Channel before setting global context :: " + channelId);
		if (StringUtils.isNotBlank(channelId))
			ExecutionContext.getCurrent().getGlobalContext().put(HeaderParam.CHANNEL_ID.name(), channelId);
		else
			ExecutionContext.getCurrent().getGlobalContext().put(HeaderParam.CHANNEL_ID.name(), "in.ekstep");

		TelemetryManager.info("Channel inside global context :: "
				+ ExecutionContext.getCurrent().getGlobalContext().get(HeaderParam.CHANNEL_ID.name()));

		if (StringUtils.isNotBlank(appId))
			ExecutionContext.getCurrent().getGlobalContext().put(HeaderParam.APP_ID.name(), appId);

		if (!isMultipart && !path.contains("/health")) {
			RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
			TelemetryManager.log("Path: " + requestWrapper.getServletPath()+ " | Remote Address: " + request.getRemoteAddr());

			ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
			requestWrapper.setAttribute("startTime", System.currentTimeMillis());
			String env = getEnv(requestWrapper);
			ExecutionContext.getCurrent().getGlobalContext().put(TelemetryParams.ENV.name(), env);
			requestWrapper.setAttribute("env", env);

			chain.doFilter(requestWrapper, responseWrapper);

			TelemetryAccessEventUtil.writeTelemetryEventLog(requestWrapper, responseWrapper);
			response.getOutputStream().write(responseWrapper.getData());
		} else {
			TelemetryManager.log("Path: " + httpRequest.getServletPath() +" | Remote Address: " + request.getRemoteAddr());
			chain.doFilter(httpRequest, response);
		}
	}

	private String getEnv(RequestWrapper requestWrapper) {
		String path = requestWrapper.getRequestURI();
		if (path.contains("/v3/definitions") || path.contains("/v3/import") || path.contains("/v3/export")
				|| path.contains("/taxonomy/")) {
			return "core";
		}
		if (path.contains("/sync/") || path.contains("v3/system") || path.contains("/v3/languages/list")) {
			return "system";
		}
		if (path.contains("/domain")) {
			return "domain";
		} else {
			return path.split("/")[3];
		}
	}

	@Override
	public void destroy() {

	}

	private String getUUID() {
		UUID uid = UUID.randomUUID();
		return uid.toString();
	}
}
