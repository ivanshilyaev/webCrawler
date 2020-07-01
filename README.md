﻿
# Test task for Softeq

🖥 “Web crawler”

### Preconditions

1.  Create your personal solution
    
2.  Focus on breadth rather than depth when cover expected output points
    
3.  Do your best to figure out industry best practices and utilize them properly
    
4.  You have a time limit - 7 days
    
5.  Tech challenge is your primary focus
    

### Problem statement

Implement a web crawler that traverses websites following predefined link depth and max visited pages limit. The main purpose of this crawler to detect the presence of some terms on the page and collect statistics, e.g.

Input (Terms): 
 
Tesla, Musk, Gigafactory, Elon Mask

Output:  

acbd.com/page2.html 8 4 0 5 17 

acbd.com/page1.html 3 2 0 2 7 a

cbd.com/page2.html 0 1 0 1 2

Clarification:  

For acbd.com/page1.html 3 2 0 2 7 

Numbers are

Tesla - 3 hits  

Musk - 2 hits 

Gigafactory - 0

hits Elon Mask - 2

hits Total - 7 hits

All stat data should be serialized into CSV file (no predefined sort). Top 10 pages by total hits must be printed to separate CSV file and console (sorted by total hits)

### Expected output

1.  Source code provided though GitHub project
    
    a.  Focus on Java 11 LTS
        
    b.  Take into account project supportability
        
    c.  Focus on documentation
        
2.  Env setup can be easily repeated
    
    a.  Add configuration and startup scripts
        
    b.  One button setup & configuration
       
	c. Prepare sample data if necessary

3.  Code follows industry best practices
    
    a. matches predefined code style - you can setup any

	b. code coverage >40%  
    
    c. contains tests of several levels - unit, integration, etc.
    
4.  Provide a link to your Demo session
    
    a.  record a video proof that app works
        
    b.  cover both the happy path and failure scenario
        
    c.  Take a code tour and clarify selected solutions
        
    d.  Prepare it in English
---

by [@ivanshilyaev](https://github.com/ivanshilyaev), 2020

