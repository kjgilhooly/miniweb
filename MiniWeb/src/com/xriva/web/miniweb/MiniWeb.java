package com.xriva.web.miniweb;

import java.io.IOException;
import java.net.ServerSocket;

public class MiniWeb
{

	public static void main(String[] args)
	{
		MiniWeb server = new MiniWeb();
		server.serve();
	}

	private void serve()
	{
		boolean running = true;
		ServerSocket s;
		try
		{
			s = new ServerSocket(8080);
			while (running)
			{
				System.out.println("Server online.");
				new MiniWebServer(s.accept()).start();
			}
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
