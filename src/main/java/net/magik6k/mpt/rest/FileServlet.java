package net.magik6k.mpt.rest;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.magik6k.mpt.db.PackageBase;
import net.magik6k.mpt.util.MptFile;

public class FileServlet extends HttpServlet{

	private static final long serialVersionUID = 7376425863179936030L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		Matcher matcher = Pattern.compile("\\/api\\/file\\/([A-Za-z0-9_\\-.~]+)(.+)").matcher(request.getRequestURI());
		
		if(matcher.matches()){
			String pack = matcher.group(1);
			String filename = matcher.group(2);
			
			System.out.println("[API]Get file "+filename+"("+pack+")");
			
			MptFile file = PackageBase.instance.getFile(pack, filename);
			
			if(file != null){
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentLength(file.content.length());
				response.getWriter().append(file.content);
			}
		}else{
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}
}
