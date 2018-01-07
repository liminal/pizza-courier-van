package se.lightside.pizza.api

import io.reactivex.Single
import retrofit2.http.*
import java.io.Serializable

// FORMAT: 1A
//
// # Pizza App API
//
// Simple backend API for a pizza ordering app.
//
// Client functionality to implement on top of this API:
// - Find the closest restaurant
// - Display the restaurant's menu
// - Let the user pick items from the menu and put them in a shopping cart
// - Place the order
// - Display order status
//

interface PizzaApi {

    /**
     * Get list of restaurants from backend
     *
     * <pre>
     *  # Restaurants [/restaurants/]
     *
     *  A list of restaurants with adress and position.
     *
     *  ## List restaurants [GET]
     *
     *  + Response 200 (application/json)
     *
     *          [
     *              {
     *                  "id": 1,
     *                  "name": "Pizza Heaven",
     *                  "address1": "Kungsgatan 1",
     *                  "address2": "111 43 Stockholm",
     *                  "latitude": 59.3360780,
     *                  "longitude": 18.0718070
     *              },
     *              {
     *                  "id": 2,
     *                  "name": "Pizzeria Apan",
     *                  "address1": "Långholmsgatan 34",
     *                  "address2": "117 33 Stockholm",
     *                  "latitude": 59.3157090,
     *                  "longitude": 18.0335070
     *              }
     *          ]
     *
     *  + Response 401
     *
     *  + Response 500
     *  </pre>
     */
    @GET("/restaurants/")
    fun getListOfRestaurants(): Single<List<PizzaRestaurant>>

    data class PizzaRestaurant(
            val id: Long,
            val name: String,
            val address1: String,
            val address2: String,
            val latitude: Double,
            val longitude: Double)

    /**
     * Get single restaurant from backend by Id
     *
     * <pre>
     * # Single restaurant by id [/restaurants/{id}]
     *
     * + Parameters
     *     + id (required, int, `23`) ... The restaurant id
     *
     * ## Get restaurant [GET]
     *
     * + Response 200 (application/json)
     *
     *         {
     *             "id": 2,
     *             "name": "Pizzeria Apan",
     *             "address1": "Ljusslingan 4",
     *             "address2": "120 31 Stockholm",
     *             "latitude": 59.3157090,
     *             "longitude": 18.0335070
     *         }
     * </pre>
     */
    @GET("/restaurants/{id}")
    fun getSingleRestaurant(
            @Path("id") id: Long
    ): Single<PizzaRestaurant>

    /**
     * Get menu for restaurant
     *
     * <pre>
     * # Menu [/restaurants/{restaurantId}/menu{?category,orderBy}]
     *
     * The food menu for a given restaurant. Parameters support filtering and ordering.
     *
     * - Parameters
     *     - restaurantId (required, int, `2`) ... The restaurant that we want to get a menu for.
     *     - category (optional, string, `Pizza`) ... Filter the menu by category value.
     *     - orderBy (optional, string, `rank`) ... Order the result by one of the fields.
     *
     * ## Get the menu [GET]
     *
     * + Response 200 (application/json)
     *
     *         [
     *             {
     *                 "id": 1,
     *                 "category": "Pizza",
     *                 "name": "Vesuvius",
     *                 "topping": [
     *                     "Tomat", "Ost", "Skinka"
     *                 ],
     *                 "price": 79,
     *                 "rank": 3
     *             },
     *             {
     *                 "id": 2,
     *                 "category": "Pizza",
     *                 "name": "Hawaii",
     *                 "topping": [
     *                     "Tomat", "Ost", "Skinka", "Ananas"
     *                 ],
     *                 "price": 79,
     *                 "rank": 1
     *             },
     *             {
     *                 "id": 3,
     *                 "category": "Pizza",
     *                 "name": "Parma",
     *                 "topping": [
     *                     "Tomat", "Ost", "Parmaskinka", "Oliver", "Färska basilika"
     *                 ],
     *                 "price": 89,
     *                 "rank": 2
     *             },
     *             {
     *                 "id": 4,
     *                 "category": "Dryck",
     *                 "name": "Coca-cola, 33cl",
     *                 "price": 10
     *             },
     *             {
     *                 "id": 5,
     *                 "category": "Dryck",
     *                 "name": "Loka citron, 33cl",
     *                 "price": 10
     *             },
     *             {
     *                 "id": 6,
     *                 "category": "Tillbehör",
     *                 "name": "Pizzasallad",
     *                 "price": 0
     *             },
     *             {
     *                 "id": 7,
     *                 "category": "Tillbehör",
     *                 "name": "Bröd och smör",
     *                 "price": 10
     *             }
     *         ]
     * </pre>
     */
    @GET("/restaurants/{restaurantId}/menu")
    fun getRestaurantMenu(
            @Path("restaurantId") restaurantId: Long,
            @Query("category") category: String? = null,
            @Query("orderBy") orderBy: String? = null
    ) : Single<List<PizzaMenuItem>>

    data class PizzaMenuItem(
            val id: Long,
            val category: String?,
            val name: String?,
            val topping: List<String>? = null,
            val price: Int,
            val rank: Int? = null)

    /**
     * Create order
     *
     * <pre>
     * # Create order [/orders/]
     *
     * Endpoint for placing an order.
     *
     * ## Place a new order [POST]
     *
     * + Request
     *
     *         {
     *             "cart": [
     *                 {
     *                     "menuItemId": 2,
     *                     "quantity": 1
     *                 },
     *                 {
     *                     "menuItemId": 3,
     *                     "quantity": 1
     *                 },
     *                 {
     *                     "menuItemId": 6,
     *                     "quantity": 2
     *                 }
     *             ],
     *             "restuarantId": 1
     *         }
     *
     * + Response 200 (application/json)
     *
     *         {
     *             "orderId": 1234412,
     *             "totalPrice": 168,
     *             "orderedAt": "2015-04-09T17:30:47.556Z",
     *             "esitmatedDelivery": "2015-04-09T17:45:47.556Z",
     *             "status": "ordered"
     *         }
    * </pre>
    */
    @POST("/orders/")
    fun createOrder(
            @Body request: PizzaOrderRequest
    ) : Single<PizzaOrder>

    data class PizzaOrderRequest(
            val restaurantId: Long,
            val cart: List<OrderItem>
    ): Serializable

    data class OrderItem(
            val menuItemId: Long,
            val quantity: Int
    ): Serializable

    data class PizzaOrder(
            val orderId: Long,
            val totalPrice: Int,
            val orderedAt: String? = null,
            val estimatedDelivery: String? = null,
            val status: String)

    /**
     * Read order
     *
     * <pre>
     * # Read order [/orders/{id}]
     *
     * Endpoint for getting order details.
     *
     * ## Get details for a given order [GET]
     *
     * - Parameters
     *     - id (required, int, `1234412`) ... The requested order id.
     *
     * + Response 200 (application/json)
     *
     *         {
     *             "orderId": 1234412,
     *             "totalPrice": 168,
     *             "orderedAt": "2015-04-09T17:30:47.556Z",
     *             "esitmatedDelivery": "2015-04-09T17:50:47.556Z",
     *             "status": "baking",
     *             "cart": [
     *                 {
     *                     "menuItemId": 2,
     *                     "quantity": 1
     *                 },
     *                 {
     *                     "menuItemId": 3,
     *                     "quantity": 1
     *                 },
     *                 {
     *                     "menuItemId": 6,
     *                     "quantity": 2
     *                 }
     *             ],
     *             "restuarantId": 1
     *         }
    * </pre>
    */
    @GET("/orders/{id}")
    fun getOrderDetails(@Path("id") orderId: Long): Single<PizzaOrderDetails>

    data class PizzaOrderDetails(
            val restaurantId: Long,
            val orderId: Long,
            val totalPrice: Int,
            val orderedAt: String? = null,
            val estimatedDelivery: String? = null,
            val status: String,
            val cart: List<OrderItem>)

}