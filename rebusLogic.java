/*
 * Rebus generator
 * By Carl Estabrook
 * 8/23/2011
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class rebusLogic 
{	
	private File fFile;
	private String sentence;
	private  ArrayList<LinkedList<String>> imageList;
	private LinkedList< String> imageNames;
	private rebusGUI m_rg;
	private String[] rebus = {"","",""};
	private String[] temprebus = {"","",""};
	private int wordscore, sentencescore = 0, wordcount = 0;
	
	public rebusLogic(rebusGUI rg, String s) 
	{
		m_rg = rg;
		sentence = s;
		imageList = new ArrayList<LinkedList<String>>();
		processImages();
		processSentence();	
	}
	private void printRebusWord()
	{
		if(wordcount == 4)
		{
			 m_rg.printChar("\n");
			wordcount = 0;
		}
		m_rg.printChar(" (");
		m_rg.printImage(rebus[0]);
		if(!rebus[1].equals(""))
		{
			m_rg.printChar(" + ");
			if(rebus[1].charAt(0)== ':')
			{
				m_rg.printImage(rebus[1].substring(1, rebus[1].length()));
			}
			else
			m_rg.printChar(rebus[1]);		
		}
		if(!rebus[2].equals(""))
		{
			m_rg.printChar(" - ");
			if(rebus[2].charAt(0)== ':')
			{
				m_rg.printImage(rebus[2].substring(1, rebus[2].length()));
			}
			else
			m_rg.printChar(rebus[2]);	
		}
		
		m_rg.printChar(") ");
		
		wordscore = scoreCalc(rebus);
		
		
		sentencescore += wordscore;
		wordcount +=1;
	}
	
	private void processImages()
	{
		fFile = new File("src/images.txt");
	    try 
	    {
			processLineByLine();
		} 
	    catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}	
	}
	//Process image names with their associated words in image.txt file
	private void processLineByLine() throws FileNotFoundException
	{
	    Scanner scanner = new Scanner(new FileReader(fFile));
	    try
	    {
	      while ( scanner.hasNextLine() )
	      {
	        processLine( scanner.nextLine() );
	      }
	    }
	    finally 
	    {	   
	      scanner.close();
	    }
	  }
	//One line of images.text
	private void processLine(String aLine)
	{
		imageNames = new LinkedList<String>();
	    Scanner scanner = new Scanner(aLine);
	  
	    while( scanner.hasNext() )
	    {
	      String name = scanner.next();
	      imageNames.add(name);
	      
	      log(name);
	    }
	    imageList.add(imageNames);	    	  	    	   
	  }
	
	private void processSentence()
	{
		if(!Character.isLetter(sentence.charAt(sentence.length()-1)))
		{
			sentence = sentence.substring(0, sentence.length()-1);
		}
			
		Scanner scanner = new Scanner(sentence);		  
	    while( scanner.hasNext() )
	    {
	      String word = scanner.next();
	      processWord(word);	      
	    } 
	    m_rg.printChar("\n");
		m_rg.printChar("sentence score = "+sentencescore);
	}
	
	private void processWord(String s)
	{
		for(int i = 0; i<3; i++)
		{
			rebus[i] = "";
			temprebus[i] = "";
		}
		wordLookup(s);
		printRebusWord();	
	}
	
	private void wordLookup(String word)
	{
		int score=100;
		for(LinkedList<String> list: imageList)
		{
	    	for(String st: list)
	    	{
	    		if(list.indexOf(st) != 0)
	    		{
	    			 wordConstruct1(word, st);
	    			int tempscore = scoreCalc(temprebus);
	    			if(tempscore <score)
	    			{
	    				score = tempscore;
	    				rebus[0] = list.get(0);
	    				rebus[1]= temprebus[1];
	    				rebus[2]= temprebus[2];
	    			}
	    			wordscore = score;
	    		}	    	
	    	}
		}
		rebus[1] = exactMatch(rebus[1]);
		rebus[2] = exactMatch(rebus[2]);  		
	}
	
	private String exactMatch(String word)
	{
		String s = ":";
		for(LinkedList<String> list: imageList)
		{
	    	for(String st: list)
	    	{
	    		if(list.indexOf(st) != 0)
	    		{
	    			 if(word.equalsIgnoreCase(st))
	    			 {
	    				 return s.concat(list.get(0));
	    			 }
	    			
	    		}	    	
	    	}
		}
		return word;	
	}
		
	private void wordConstruct1(String word, String st)
	{
		String addString = "", minusString = "";
		ArrayList<String> imageName = new ArrayList<String>();
		for(int i = 0; i< word.length(); i++)
		{
			imageName.add(i, "");
		}
		boolean found =false;
		int l = 0;
		String[] bestrebus = {"","",""};
		String[] temrebus = {"","",""};
		int rescore = 100, tempscore;
		
		for(int g = 0; g < st.length(); g++)
		{
		    temrebus[1] = "";
		    temrebus[2] = "";
		    addString = "";
		    minusString = "";
		    nullOut(imageName, word);
		    l=0;
		    
		    if(g>0)
		    {
		    	for(int y =0; y<g; y++)
		    	{
		    		minusString = minusString.concat(st.substring(y, y+1));	
		    	}
		    }
		    
		    for(int i = g; i < st.length(); i++)
			{ 	    	
				found = false;
				for(int j = l; j < word.length();j++)
				{
					if(st.substring(i, i+1).equalsIgnoreCase (word.substring(j, j+1)))
					{
						found = true;
						if(imageName.get(j) == "")
						{
							boolean legal = true;
							for(int q = j+1; q< imageName.size(); q++)
							{
								if(imageName.get(q)!= "")
								{
									legal = false;		
								}							
							}
							if(legal == true)
							{
								imageName.set(j, word.substring(j, j+1));
								l = j;
								break;
							}							
						}
						else
						{							
							minusString = minusString.concat(imageName.get(j));			
						}							
					}					
				}
				if(!found)
				{
					minusString = minusString.concat(st.substring(i, i+1));
				}
			}
			for(int i =0; i < imageName.size(); i++)
			{
				if(imageName.get(i)== "")
				{					
					addString = addString.concat(word.substring(i, i+1));  
				}
			}			
			temrebus[1] = addString;
			temrebus[2] = minusString;
			tempscore = scoreCalc(temrebus);
			
			if(tempscore<rescore)
			{
				rescore = tempscore;
				bestrebus[1] = temrebus[1];							
				bestrebus[2] = temrebus[2];
			}					     
		}
	    temprebus[1] =bestrebus[1];
	    temprebus[2] =bestrebus[2];		
	}
	
	private void nullOut(ArrayList<String> imageName, String st)
	{ 
		for(int i = 0; i< st.length(); i++)
		{
			imageName.set(i, "");
		}		
	}
	
	
	private int scoreCalc(String[] a)
	{
		int score = 0;
		for(int i = 1; i< a.length; i++)
		{
			String s = a[i];
			if(s!= "" && !(s.substring(0, 1).equals(":")))
			{
				for(int j = 0; j < s.length(); j++)
				{
				    if(Character.isLetter(s.charAt(j)))
				    {
				    	char ch = s.charAt(j);
				    	if(ch=='A'||ch=='a'||ch=='E'||ch=='e'||ch=='I'||ch=='i'||ch=='O'||ch=='o'||ch=='U'||ch=='u')
				    		score+=1;
				    	else
				    		score+=5;			    	
				    }
				    else
				    {
				    	
				    }
				}			
			}
		}
		return score;
	}
	
	private static void log(Object aObject)
	{
	    //System.out.println(String.valueOf(aObject));
	}

}
