package code.excercise.service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import code.excercise.dao.UserDao;
import code.excercise.domain.User;
import code.excercise.util.EmailUtil;

@Path("/user")
public class UserService {
	UserDao userDao = new UserDao();
	
	// Implementation has been limited to JSON
	@GET
	@Path("/{userId}")
	@Produces({MediaType.APPLICATION_JSON})
	public User getUser(@PathParam("userId") int userId) {
		return userDao.getUser(userId);
	}
	
	@GET
	@Path("/confirm/{userId}/{log}")
	@Produces({MediaType.APPLICATION_JSON})
	public User confirmUser(@PathParam("userId") int userId,@PathParam("log") long log) {
		if((System.currentTimeMillis()-log)<15*60*1000)
		{
			System.out.println(userId);
			User user  = userDao.getUser(userId);
			System.out.println(user.toString());
			return user;
		}
		
		return null;
	}
	
	
	@POST
	@Path("/register")
	@Produces({MediaType.APPLICATION_JSON})
	public User addUser(User user) {
		User u = userDao.addUser(user);	
		if(!isValidEmailAddress(user.getEmail()))
			return null;
		sendActivationEmail(u);
		return userDao.addUser(u);		
	}
	
	private void sendActivationEmail(User user)
	{
		String link = "http://localhost:8080/coveiot/api/user/confirm/"+user.getUserId()+"/"+System.currentTimeMillis(); 
		StringBuilder bodyText = new StringBuilder(); 
		 bodyText.append("<div>")
              .append("  Dear User<br/><br/>")
              .append("  Thank you for registration. Your mail ("+user.getEmail()+") is under verification<br/>")
              .append("  Please click <a href=\""+link+"\">here</a> ")
              .append("  <br/><br/>")
              .append("  Thanks,<br/>")
              .append("  Admin Team")
              .append("</div>");
		      String [] to = {user.getEmail()};
		      
			  EmailUtil.sendFromGMail("dummmyuser123456","coveiot123",to, "Welcome to coveiot", bodyText.toString());
			
	}
	
	private static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}
}
