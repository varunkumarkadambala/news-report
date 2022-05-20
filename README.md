#News Report Generator
The idea behind this project is to process new articles that are provided by the news service that can be forked from:
https://github.com/cheche200/news-service

The aim of the project is to develop a solution in Java that can read and process 10 articles from the news-service API.



-          If the article belongs to the SOCIAL category it should be ignored.



-          The output of the solution should be a report in a .txt file called edited_articles.txt  that must have the following format:

For each article it should have a new row with the below values:

-          Last 10 characters of the article ID.

-          Category

-          Title

-          Number of tags

-          Weight



The rows should be order by Weight in a descending order.