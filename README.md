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

* User can create a new account
    *  User can take a picture and set it as their profile picture
* User can login
* User can see a stream of bikes
* User can create a post for putting a bike for rent (bike price, details, availability, contact information, upload bike pic)

**Optional Nice-to-have Stories**

* Forget password button
* Internal Messaging Tool

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


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Tab at top of screen
* Navigates to Compose screen

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
- Google Maps API Base URL https://www.google.com/maps/search/?api=1[api_key] (displaying )


-----

# Workout Social Media

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

#### Workout Social Media:
- **Description**: Friends can connect and compete to see who has the highest workout score for a day
#### Workout Social Media
- **Category**: Lifestyle & Social
- **Mobile**: Users can share the workout they completed that day
- **Story**: Allows people to discover new workouts based on what their friends like to do and encourages friendly competition as a source of motivation
- **Market**: Anyone interested in friendly competition with friends
- **Habit:** Allows friends to compete with one another, wanting them to work out more
- **Scope**: V1 would allow users to make an account and share their workout. V2 would display details about the profile


## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can make account
* User can login
* User can upload a workout description
* User can see feed of their followers workouts

**Optional Nice-to-have Stories**

* Can see details about profile (favorite exercises, youtubers, tips)

### 2. Screen Archetypes

* Initial screen
   * User selects to login or register
* Login
   * User can login
* Register
    * User can register
* Compose
    * User can upload a workout description
* Stream
    * User can see feed of their followers workouts
* User details [Optional]
    * Details about profile


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Compose
* Stream
* [Optional] User details

**Flow Navigation** (Screen to Screen)

* Initial
   * Login
   * Register
* Login
   * Stream
* Register
    * Stream
* Compose
    * Stream
* Stream
    * None
* User Details
    * None

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
