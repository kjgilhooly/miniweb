package com.xriva.web.miniweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;

public class MiniWebServer extends Thread
{
	private Socket client;

	public MiniWebServer(Socket client)
	{
		super("MiniWebServer");
		this.client = client;
	}

	public void run()
	{
		System.out.println(Thread.currentThread().getId() + " started.");
		try
		{
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			String line;
			String[] request = new String[50];
			int x = 0;
			String rc = "404 Not Found";
			/*
			 * get the entire request body to display later
			 */
			while ((line = in.readLine()) != null)
			{
				if (line.equals(""))
					break;
				request[x++] = line;
			}
			client.shutdownInput();
			/*
			 * get the file name
			 */
			System.out.println("-- Request: " + request[0]);
			if (request[0] != null)
			{
				String command = request[0].substring(0,
						request[0].indexOf(' '));
				String resource = request[0].substring(
						request[0].indexOf('/') + 1,
						request[0].lastIndexOf(' '));
				if (resource.equals(""))
					resource = "our default file";
				if (command.equals("GET") || command.equals("HEAD"))
				{
					rc = "404 Not Found";
				}
				else
					rc = "501 Not Implemented";
				/*
				 * output (rather) elaborate apology page
				 */
				out.println("HTTP/1.1 " + rc);
				out.println("Server: MiniWeb/1.0");
				out.println("Content-Type: text/html; charset=utf-8");
				out.println("Connection: Close");
				out.println("");
				out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML Basic 1.1//EN\" \"http://www.w3.org/TR/xhtml-basic/xhtml-basic11.dtd\">");
				out.println("<html>");
				out.println("<head>");
				out.println("<meta charset=\"UTF-8\">");
				out.println("<title>MiniWeb</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>MiniWeb</h1>");
				out.println("<h2>A Small Web Server at ");
				out.println(InetAddress.getLocalHost().getHostAddress() + "</h2>");
				out.println("<h3>Local Time ");
				out.println(DateFormat.getDateTimeInstance().format(new Date()) + "</h3>");
				out.println("<table style=\"width:60%\" border=\"1\">");
				out.println("<tr>");
				out.println("<td>");
				if (command.equals("GET") || command.equals("HEAD"))
				{
					out.println("As far as we can tell, <i>" + resource
							+ "</i> is not available on ");
					out.println(InetAddress.getLocalHost().getHostName() + ".");
					out.println(" We apologize for any inconvenience.");
				}
				else
				{
					out.println("I only know how to HEAD and GET.<br>");
					out.println("That's why I'm called MiniWeb.");
				}
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("<p>Below, we've shown what you asked us, in case you want to try <a href=\"http://www.google.com\">Google</a> or somebody else.");
				out.println("<ul>");
				for (int y = 0; y < x; y++)
				{
					out.println("<li>" + request[y] + "</li>");
				}
				out.println("</ul>");
				out.println("</body>");
				out.println("</html>");
				out.flush();
				out.close();
				in.close();
				client.close();
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getId() + " ended.");
		return;
	}
}
