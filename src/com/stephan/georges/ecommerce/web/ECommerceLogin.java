package com.stephan.georges.ecommerce.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class ECommerceLogin
 */
@WebServlet("/login")
public class ECommerceLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ECommerceLogin.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ECommerceLogin() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String body = request.getReader().lines().collect(Collectors.joining());
		LOGGER.info("Got:" + body);

		JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

		String username = jsonObject.get("username").getAsString();
		String password = jsonObject.get("password").getAsString();

		// Connect Rest
		URL url = new URL("http://web-as:8080/authenticate");
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "application/json");

		StringBuilder jsonRequest = new StringBuilder();
		jsonRequest.append("{\r\n\t");
		jsonRequest.append("\t\"userName\": \"");
		jsonRequest.append(username);

		jsonRequest.append("\",");
		jsonRequest.append("\t\"password\": \"");
		jsonRequest.append(password);
		jsonRequest.append("\"\r\n");
		jsonRequest.append("}\r\n");

		System.out.println(jsonRequest.toString());

		try (OutputStream os = httpConnection.getOutputStream();) {
			os.write(jsonRequest.toString().getBytes());
			os.flush();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,e.getMessage(),e);
		}

		if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
		}

		StringBuilder buffer = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));) {
			String output;
			while ((output = br.readLine()) != null) {
				buffer.append(output);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,e.getMessage(),e);
		}

		response.getWriter().write(buffer.toString());

	}

}
