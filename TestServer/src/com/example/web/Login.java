package com.example.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Login() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type","text/html;charset=utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String name=request.getParameter("name");
		String pass=request.getParameter("pass");
//		System.out.println(java.nio.charset.Charset.defaultCharset());
//		System.out.println(name1);
//		System.out.println(pass1);
//		String name=new String(request.getParameter("name").getBytes(),"GBk");
//		String pass=new String(request.getParameter("pass").getBytes("GBK"),"UTF-8");
//		System.out.println(name);
//		System.out.println(pass);
//		String name2=new String("张三".getBytes("UTF-8"),"UTF-8");
//		System.out.println(name2);
//		System.out.println(name!=null);
//		System.out.println(pass!=null);
//		System.out.println(new String("zhangsan".getBytes(),"GBK"));
//		System.out.println(name.equals(new String("zhangsan".getBytes(),"GBK")));
//		System.out.println(pass.equals("1234"));
//		if(name!=null && pass!=null && name.equals(new String("张三".getBytes(),"GBK"))&& pass.equals(new String("1234".getBytes(),"GBK"))){
		if(name!=null && pass!=null && name.equals("11")&& pass.equals("11")){	
		out.print(1);
		}
		else{
			out.print(0);}
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		doGet(request,response);
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
