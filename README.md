Original App Design Project - README Template
===

# AirBNB for Bikes

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
#### Airbnb for bikes & scooters:
- **Description:** Bike owners and scooter owners can put their bike/scooter up for sale and users can rent the bike/scooter from them

#### Student Matching App:
- **Description**: Connects incoming college freshmen to other incoming freshmen in their area so that students can get to know each other before they begin college


### App Evaluation
[Evaluation of your app across the following attributes]

#### Airbnb for bikes & scooters:
- **Category**: Lifestyle
- **Mobile**: Users are able to browse available bikes and scooters in close proximity to them, organizing the layout by location, price, seller reputation
- **Story**: Connects people who need eco friendly transportation to people who are willing to rent theirs
- **Market**: The market for this would be very large, as a lot of people use bikes and scooters on the daily, especially on large college campuses.
- **Habit:** This app is not necessarily addictive, but rather the need for transportation and the addictiveness of biking will cause users to use this app. Users can save money by avoiding to purchase a bike or scooter. Bike owners will use this to make more money off of their bike or scooter
- **Scope**: V1 would allow bike owners and bike seekers to make a profile. V2 would allow bike seekers to view bike owners bikes in the area. V3 would incorporate some third party messaging tool (opens email). V4 would incorporate an internal messaging tool



## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [X] User can create a new account
    *  User can take a picture and set it as their profile picture
* [X] User can login
    * [X] User is still logged in when they close app
* [X] User can see a stream of bikes
* [X] User can create a post for putting a bike for rent (bike price, details, availability, contact information, upload bike pic)
* [X] User can logout and see their posts on the profile Fragment
* [X] User can filter specific bikes using search Fragment (complex algo)
* [X] User can click on a post to see a detail view)
* [X] User can zoom in on bike images in PostDetails


**Optional Nice-to-have Stories**

* [X] ProfileFragment shows posts in grid format
    * [X] User wont see their username and profile in each individual post in recyclerview
* [X] get rid of searchfragment and add it into the home
* [X] Refresh Feature on home feed
* [X] Infinite scroll on home feed
* [X] User can take a profile picture with camera
* [X] User can upload picture from gallery
* [X] User can pick profile picture from gallery
* Use spinner for displaying condition of the bike
* Settings page where user can change their username
* User can edit a post that they have made
* User can delete their post
* Relative distance
* Swipe to switch views
* Swipe up to open GoogleMaps displaying location of bike
* Forget password button
* Internal Messaging Tool
* Users can add ratings for people who have posts
* Implement Likes on a specific bike
* Add rating to other users

### 2. Screen Archetypes

* Initial screen
   * Asks user to log in or sign up (two separate buttons)
* Login
   * User can log in
* Register
   * User can register & create an account
* Stream
    * User can see available bikes in their area
* Compose
    * User can create a post for putting a bike for rent (bike price, details, availability, contact information, upload bike pic)
* Detail View
    * User can see bike details, bike picture, and bike location (from GoogleMaps API), and has a button in which user can click to contact owner
* Profile View
    * User can logout
* Search View
    * User can filter posts


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Bottom Navigation
    * Home View
    * Search View
    * Compose View
    * Profile View

**Flow Navigation** (Screen to Screen)

* Initial Screen
   * Login
   * Register
* Login
   * Stream
* Register
    * Stream
* Stream
    * None
* Compose
    * Stream
* Profile
    * None
* Search
    * None

## Wireframes
[Add picture of your hand sketched wireframes in this section]
![](https://i.imgur.com/GSCiGBn.png)


### [BONUS] Digital Wireframes & Mockups
https://www.figma.com/file/06wK69DFoCKtWoQrf5ChPn/Cycleshare?node-id=0%3A1

### [BONUS] Interactive Prototype
https://www.figma.com/proto/06wK69DFoCKtWoQrf5ChPn/Cycleshare?node-id=1%3A2&scaling=min-zoom

## Schema
### Models

Bike Post

| Property | Type     | Description |
| -------- | -------- | -------- |
| objectId| String| unique id for user post|
|author | pointer to user|tells who created the post|
|profilePicture |pointer to user|displays profile picture|
|image |file | picture of the bike|
|createdAt|DateTime|date when post is created (default field)|
|updatedAt|DateTime|date when post is updated (default field)|
|Location|String|Location of the bike|
|Price|Number|price of the bike|
|Description|String|Details about the bike|

User
| Property  | Type     | Description |
| --------  | -------- | -------- |
| objectId| String| unique id for user post|
| email     | String     | email unique to a user     |
|profilePicture| File| Profile picture unique to user|
|password|String|password for login|
|createdAt|DateTime|date when post is created (default field)|
|updatedAt|DateTime|date when post is updated (default field)|

### Networking

[Add list of network requests by screen ]
- Login
    - (Read/CHECK) if user credientials are authorized
- Signup
    - (Create) new User with user credentials
- Home
    - (Read/GET) all posts

> [let query = PFQuery(className:"Post")
    query.order(byDescending: "createdAt")
    query.findObjectsInBackground { (posts: [PFObject]?, error: Error?) in
    if let error = error {
        print(error.localizedDescription)
    }
    else if let posts = posts {
        print("Successfully retrieved \(posts.count) posts.")
        //display posts in recycler view
    }
    }]
- Create Post Screen
    - (Create/POST) Create a new post object
- Detail View
    - (Create) User can email other users
- Search
    - (Read/Get) Search for entries based on location (Complex Algo)
> [let query = PFQuery(className:"Post")
    query.whereKey("location", equalTo: current location) //obtained through API
    query.order(byDescending: "createdAt")
    query.findObjectsInBackground { (posts: [PFObject]?, error: Error?) in
    if let error = error {
        print(error.localizedDescription)
    }
    else if let posts = posts {
        print("Successfully retrieved \(posts.count) posts.")
        //display search resultsby location
   }
}]


[OPTIONAL: List endpoints if using existing API such as Yelp]
- Google Maps API Base URL https://www.google.com/maps/@?api=1&map_action=map[api_key] (displaying a map)
    -
- Google Maps API Base URL https://www.google.com/maps/search/?api=1[api_key]
