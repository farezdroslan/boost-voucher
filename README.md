# Voucher Pool Concept

A voucher pool is a collection of unique voucher codes that
customers can use to receive discounts on a webshop. Each code can only be used once and must track the date of its usage.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Setup Instructions](#setup-instructions)
3. [APIs](#apis)
   - [Endpoints](#endpoints)
   - [Sample Requests and Responses](#sample-requests-and-responses)

## Prerequisites
- Java 17
- Apache Maven 3.9.9

## Setup Instructions

1. **Clone the repository:**

    ```bash
    git clone https://github.com/farezdroslan/boost-voucher.git
    cd voucher
    ```

2. **Configure the Database**

    ```bash
    Update application.properties under src/main/resources.

    Code for application.properties
    ```

    ```bash
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driver-class-name=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.h2.console.enabled=true
    spring.jpa.hibernate.ddl-auto=update
    ```

## APIs

### Endpoints

|Endpoint                                     | Method   |  Description                    |
|---------------------------------------------|----------|---------------------------------|
|`/api/voucher/generate`                      | POST     | Generate a voucher              |
|`/api/voucher/validate`                      | POST     | Validate voucher code                                                                                       |
|`/api/voucher/valid`                         | GET      | Returns all valid voucher code                               |


## Sample Requests and Responses

1. **Generate voucher codes**

    **Request**


    GET [http://localhost:8080/api/voucher/generate]


    **Response**

    ```json
    [
        {
            "id": 1,
            "code": "D5856FE5",
            "recipientName": "John Doe",
            "email": "john.doe@example.com",
            "specialOfferName": "WinterSale",
            "discountPercentage": 30,
            "expirationDate": "2025-12-31",
            "usageDate": null
        }
    ]
    ```

2. **Validate voucher code**

    **Request**

    POST [http://localhost:8080/api/voucher/validate?code=D5856FE5&email=john.doe@example.com]
    Content-Type: application/json

    **Response**
    
    ```json
    {
        "Special Name": "WinterSale",
        "Percentage": 30
    }
    ```

3. **Get all valid vouchers by a given email**

    **Request**

    GET [http://localhost:8080/api/voucher/valid?email=john.doe@example.com] 
    Content-Type: application/json

    **Response**

    ```json
    [
        {
            "id": 1,
            "code": "466C4746",
            "recipientName": "John Doe",
            "email": "john.doe@example.com",
            "specialOfferName": "SummerSale",
            "discountPercentage": 20,
            "expirationDate": "2025-12-31",
            "usageDate": null
        },
        {
            "id": 2,
            "code": "D5856FE5",
            "recipientName": "John Doe",
            "email": "john.doe@example.com",
            "specialOfferName": "WinterSale",
            "discountPercentage": 30,
            "expirationDate": "2025-12-31",
            "usageDate": null
        }
    ]
    ```

