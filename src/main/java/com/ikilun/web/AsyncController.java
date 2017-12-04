package com.ikilun.web;

import java.util.concurrent.ConcurrentLinkedDeque;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class AsyncController {
	private ConcurrentLinkedDeque<DeferredResult<String>> deques = new ConcurrentLinkedDeque<DeferredResult<String>>();

	@RequestMapping("/testDeferredReq.xhtml")
	@ResponseBody
	public DeferredResult<String> testRefered(HttpServletRequest request) {
		DeferredResult<String> dr = new DeferredResult<>(20000L);
		deques.addFirst(dr);
		dr.onCompletion(new Runnable() {
			public void run() {
				// 响应完毕之后，将请求从队列中去除掉
				System.out.println("异步调用完成...");
				deques.remove(dr);
			}
		});
		dr.onTimeout(new Runnable() {
			public void run() {
				//超时
				System.out.println("异步调用超时..");
				dr.setResult("time out");
				deques.remove(dr);
			}
		});
		return dr;
	}
	
	@RequestMapping("/testDeferredRes.xhtml")
	@ResponseBody
	public String testDeferredRes(int i, HttpServletRequest request) {
		for(DeferredResult<String> dr : deques){
			dr.setResult(i+"");
		}
		return "success";
	}
}
