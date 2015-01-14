package net.magik6k.mpt.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.db.PackageBase.PackageSummary;
import net.magik6k.mpt.util.MptFile;

public class PackageServlet extends HttpServlet{

	private static final long serialVersionUID = -3668869268134596227L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("[API]Get package " + req.getRequestURI().replaceFirst(".+/", ""));
		
		PackageSummary summary = PackageBase.instance.getSummary(req.getRequestURI().replaceFirst(".+/", ""));
		if(summary != null){
			resp.setStatus(HttpServletResponse.SC_OK);
			StringBuilder res = new StringBuilder("{name=\"");
			res.append(summary.name).append("\",repo=\"");
			res.append(summary.repo).append("\",checksum=\"");
			res.append(summary.chechsum).append("\",files={");
			
			for(MptFile file : PackageBase.instance.getFiles(summary.name)){
				res.append("\"").append(file.name).append("\",");
			}
			res.append("nil},dependencies={");
			
			for(String dep : PackageBase.instance.getDependencies(summary.name)){
				res.append("\"").append(dep).append("\",");
			}
			res.append("nil}}");
			
			resp.setContentType("text/lua");
			resp.setContentLength(res.length());
			resp.getWriter().append(res);
		}else{
			resp.sendError(404);
		}
		
	}
	
}
