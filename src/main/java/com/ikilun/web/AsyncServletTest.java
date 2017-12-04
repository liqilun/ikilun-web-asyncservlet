package com.ikilun.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet(urlPatterns="/asyncTest", asyncSupported=true)
public class AsyncServletTest extends HttpServlet {
	private ExecutorService service = Executors.newFixedThreadPool(10); 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter pw = resp.getWriter();
		pw.println("hello");
		AsyncContext context = req.startAsync();
		service.submit(new Thread(new ThreadTest(context)));
		//pw.flush();
		//pw.close();
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	
}
class ThreadTest implements Runnable{
	private AsyncContext context;
	public ThreadTest(AsyncContext context){
		super();
		this.context = context;
	}
	public void run() {
		ServletResponse res = context.getResponse();
		try {
			Thread.sleep(10000);
			PrintWriter pw = res.getWriter();
			pw.println("thead hello");
			//pw.flush();
			//pw.close();
			context.complete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}