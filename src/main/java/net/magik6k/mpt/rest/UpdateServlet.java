package net.magik6k.mpt.rest;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.db.PackageBase.PackageSummary;

public class UpdateServlet extends HttpServlet{

	private static final long serialVersionUID = 8072590318077243996L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuilder responseBuilder = new StringBuilder("{");
		for(Entry<String, String[]> check : request.getParameterMap().entrySet()){
			PackageSummary summary = PackageBase.instance.getSummary(check.getKey());
			if(summary != null){
				if(!check.getValue()[0].equals(summary.chechsum)){
					responseBuilder.append("{package=\"").append(check.getKey())
						.append("\",checksum=\"").append(summary.chechsum).append("\"},");
				}
			}else{
				responseBuilder.append("{package=\"").append(check.getKey()).append("\",checksum=nil},");
			}
		}
		responseBuilder.append("nil}");
		response.setContentType("text/lua");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentLength(responseBuilder.length());
		response.getWriter().append(responseBuilder);
	}
}
