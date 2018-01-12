package env.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 
 * @author Idryss Bourdier
 * Gere la liste d'action possible, afin que
 * le generateur puisse piocher et en effectuer
 * aleatoirement.
 * 
 */

public enum EActionList {    
	ACTION,
	METTRE,
	PLACER;
    
    EActionList() {}
    
    public String getContent(EActionList Flag) {
    	String res = "";
    	switch(Flag) {
    		case METTRE :
    			res = String.format("Mettre %s %s %s %s %s",
    					"Jouet",
    					"unjouet",
    					"Livres",
    					(int)(Math.random() * (1000)) + 1,
    					LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
    					);
    			break;
    		case PLACER :
    			res = String.format("Placer %s %s",
    					(int)(Math.random() * (10)) + 0,
    					(int)(Math.random() * (1000)) + 1
    					);
    			break;
    	}
    	return res;
    	
    }
}
