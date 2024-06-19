**[BOOKBITE API](https://github.com/Peter-cloud-web/BookBites)**

**This is the serverside web service API for the BookBite Application. An application that will enable users to share hardcopy books with their peers in exchange for another. Users will post a book with the title, category, and a short preview of the book and a cover photo and interested parties will bid for the book and user chooses who to offer.**

## **Authorization and Authentication**

This API uses token based authentication with JWT. Username and password must be provided and bearer header to access data on every request made to the server.

**Authorization and Authentication Endpoints** 
Registration  - [/v1/register](http://localhost:8080/v1/register)
Login - [/v1/login](http://localhost:8080/v1/login)

## **Endpoints**

 - User profile -  [/v1/getLoggedInUser](http://localhost:8080/v1/getLoggedInUser)
 - User details - [/v1/getUserDetails{email}](http://localhost:8080/v1/getUserDetails%7Bemail%7D)
 - Create Book post - /[v1/create](http://localhost:8080/v1/create)
 - Get all posted books - [/v1/allBooks](http://localhost:8080/v1/allBooks)
 - Get book details - [/v1/getBooksById/{id}](http://localhost:8080/v1/getBooksById/%7Bid%7D)

**Filter books** 

 - Get all books categories - [/v1/getAllCategories](http://localhost:8080/v1/getAllCategories)
 - Get books by Category - [/v1/getBooksByCategory/{category}](http://localhost:8080/v1/getBooksByCategory/%7Bcategory%7D%7Bemail%7D)
 - Get all locations - [/v1/getAllLocations](http://localhost:8080/v1/getAllLocations) 
 - Get books by location - [/v1/getBooksByLocation/{location}](http://localhost:8080/v1/getBooksByLocation/%7Blocation%7D)

