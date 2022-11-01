package utils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;

import io.jsonwebtoken.*;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

public class JWT 
{

	 	private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
	 	private static final Logger logger = LoggerFactory.getLogger(JWT.class);
	 	
		public static String createJWT(String id, String issuer, String subject, long ttlMillis, String username, String faction,String firstname, String lastname) 
		{
			//The JWT signature algorithm we will be using to sign the token
		    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		    long nowMillis = System.currentTimeMillis();
		    Date now = new Date(nowMillis);
		
		    //We will sign our JWT with our ApiKey secret
		    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
		    logger.debug("Username for JWT:" + username);
		    logger.debug("Firstname for JWT:" + firstname);
		    logger.debug("Lastname for JWT:" + lastname);
		    
		    //Let's set the JWT Claims
		    JwtBuilder builder = Jwts.builder().setId(id)
		            .setIssuedAt(now)
		            .setSubject(subject)
		            .setIssuer(issuer)
		            .claim("username", username)
		            .claim("faction", faction)
		            .claim("firstname", firstname)
		            .claim("lastname", lastname)
		            .signWith(signatureAlgorithm, signingKey);
		  
		    //if it has been specified, let's add the expiration
		    if (ttlMillis > 0) {
		        long expMillis = nowMillis + ttlMillis;
		        Date exp = new Date(expMillis);
		        builder.setExpiration(exp);
		    }  
		  
		    //Builds the JWT and serializes it to a compact, URL-safe string
		    return builder.compact();
		}
		public static Claims decodeJWT(String jwt) 
		{
			    //This line will throw an exception if it is not a signed JWS (as expected)
			    Claims claims = Jwts.parser()
			            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
			            .parseClaimsJws(jwt).getBody();
			    
			    logger.debug("Claims: " + claims.get("username"));
			    logger.debug("Claims: " + claims.get("firstname"));
			    logger.debug("Claims: " + claims.get("lastname"));
			    return claims;
		}
}