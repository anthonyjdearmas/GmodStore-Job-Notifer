 <img src="https://i.gyazo.com/9485d7571b45399473a1844f82b39940.png" height="200" width="700">


# What do I use this for?
This program was created to allow freelance GLUA developers to know about newly created jobs on the GmodStore market place without having to manually refresh the page every ten minutes. The major advantage of using this program is that you will know about jobs before any other developers apply for the newly posted job!

# How does it work?
The program works by doing something called 'web-scraping'. Basically, it grabs the HTML of given web pages and parses through it for desired information. The web-scraping library used to power this program is called [JSoup](https://jsoup.org/apidocs/).

# How do I run It
The easiest way to run the program is to simply double click on the .exe file found within the zip file. Just extract the the .exe file to your desired location and run it. It's that easy! You can delete the other files if you would like to. They are just if you would like to take a look at the source code or modify the program yourself.


# Primary features

 - Super easy-to-use and intuitive interface that allows you to visit jobs on the fly
 - Manual action buttons that allow you to start, stop or pause the program
 - Various settings that can changed as the program runs
		 - Refresh settings
		 - Notification settings
		 - Log settings
 - Console log that allows you to see anything that you missed when not looking at the program
 - A progress bar that displays exact time until next refresh
 - Helpful tool tips that help display important info like instructions or longer text


# Frequently asked questions
**Question:** When I double click on the program, I get an error saying *"A JNI error has occurred please check your installation and try again."* 
**Answer:** This error can be the result of various things. However, the most common reason for this is that your computer is trying to run the program with two different versions/types of java. The program is designed to run on either Java's JDK *OR* JRE. Try uninstalling the JRE and run the program again.

**Question:** Why does it take so long to load new jobs?
**Answer:** The program's speed is heavily dependent upon your internet connection speed. If your internet is slow then the program will be slow too!

**Question:** How can I see more than 7 jobs?
**Answer:** The program was designed to only show the latest jobs. The 8th job and so on are not that recent and not considered to be the latest job listings. On average, there are only 3-5 new job postings a day on the market.

**Question:** I see that GmodStore has its own API, why not use that to grab the information instead of web-scraping?
**Answer:** This would be a fantastic idea, however GmodStore's API does not have anything for getting information about job postings. For this reason, the API would be useless and web-scraping is needed.
