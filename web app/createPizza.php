<?php
session_start();
$name = $_GET['name'];
?>

<!DOCTYPE html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- CSS Libraries -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.2/css/all.css"
        integrity="sha256-piqEf7Ap7CMps8krDQsSOTZgF+MU/0MPyPW2enj5I40=" crossorigin="anonymous" />
    <!-- Main CSS Stylesheet -->
    <link rel="stylesheet" href="style.css">
    <link rel="shortcut icon" href="img/favicon.ico">
    <title>PAAS Co - We Come In Peace For Pizza</title>
</head>

<body>
    <!-- Where the FUN start! -->
    <?php require_once 'process_order.php' ?>

    <!-- Navigation ** includes mobile navigation ** -->
    <section class="navigation">
        <header>
            <div class="logo">
                <a href="index.html"><img src="img/pizza.png" alt="PAAS Co Logo"></a>
            </div>
            <nav id="main-nav" class="nav">
                <ul>
                    <li><a href="index.html">Home</a></li>
                    <li><a class="active" href="setUpOrder.php">Order</a></li>
                    <li><a href="aboutPage.php">About</a></li>
                    <li><a href="contactPage.php">Contact</a></li>
                </ul>
            </nav>
            <div class="menu-toggle"><i class="fas fa-bars"></i><i class="fas fa-times swap"></i></div>
        </header>
    </section>

    <!-- Background Galaxy Canvas -->
    <canvas id="bgcanvas"></canvas>

    <!-- Order Submit Modal -->
    <div class="modal fade" id="orderSubmit" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content bg-dark">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLongTitle">Please confirm your order</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <!-- order confirmed and will be stored to DB -->
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="confirmOrder()">Beam
                        em' up!</button>
                    <!-- back to the pizzaMaker screen, not clearing order list -->
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <hr class="bg-light">
                    <p class="my-4"></p>
                    <h5>Your Order Details</h5>
                    <p class="my-4"></p>
                    <h6 id="finalAmount"></h6>
                    <p class="my-4"></p>
                    <h6>Order Items:</h6>
                    <p class="my-4"></p>
                    <ul id="pizzasList">
                        <!--this is where we list the ingredients that were out of stock at the time pizza save is clicked -->
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <!-- Cancel Order Modal -->
    <div class="modal fade" id="orderCancel" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content bg-dark">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLongTitle">Are you sure you want to cancel?</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <!-- order confirmed and will be stored to DB -->
                    <button type="button" class="btn btn-secondary" data-dismiss="modal"
                        onclick="cancelOrder()">Yes</button>
                    <!-- back to the pizzaMaker screen, not clearing order list -->
                    <button type=" button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>


    <!-- Create Order Main Content -->
    <section class="create-page-intro">
        <div class="container">
            <div class="row">
                <div class="col-xl-7">
                    <hr class="my-2">
                    <h1 class="welcome">Create Your Pizza</h1>
                    <p class="lead muted-text d-none" id="customerName"><?php echo ($name) ?></p>
                    <hr class="my-2">
                    <p class="lead muted-text">Select any 6 toppings and save your Pizza to your order. Repeat as
                        needed.
                    </p>
                    <hr class="my-2">
                    <!-- Pizza Maker Canvas -->
                    <div class="canvasparent">&nbsp; <canvas id="pizzamaker"></canvas></div>

                    <button type="addpizza" class="btn addpizza" id="addpizza" onclick="addPizza()">Save Pizza To
                        Order</button>

                    <form>
                        <!-- <hr class="my-3">
                        <p class="lead" style="color:#68b4fd;">Base</p>
                        <hr class="bg-light">
                        <div id="divBase">

                        </div>
                        <hr class="my-2">
                        <p class="lead" style="color:#68b4fd;">Sauce </p>
                        <hr class="bg-light">
                        <div id="divSauce">

                        </div> -->
                        <hr class="my-2">
                        <p class="lead" style="color:#68b4fd;">Cheese</p>
                        <hr class="bg-light">
                        <div id="divCheese">
                            <!-- use js function populate Cheese -->
                        </div>
                        <hr class="my-2">
                        <p class="lead" style="color:#68b4fd;">Meat</p>
                        <hr class="bg-light">
                        <div id="divMeat">
                            <!-- use js function populate Meat -->
                        </div>
                        <hr class="my-2">
                        <p class="lead" style="color:#68b4fd;">Veggies</p>
                        <hr class="bg-light">
                        <div id="divVeggies">
                            <!-- use js function populate Veggies -->
                        </div>
                    </form>
                    <hr class="my-4">

                </div>
                <!--display order items here-->
                <div class="col-xl-5 border-top border-bottom rounded bg-dark">
                    <p class="my-4"></p>
                    <h3>Order Items</h3>
                    <p class="my-4"></p>
                    <div class="orderitemslist" id="orderlist">
                    </div>

                    <!--display and update order total here-->
                    <div class="ordertotalsection">
                        <hr class="my-4">
                        <h3>Order Total:</h3>
                        <p class="lead" id="ordertotal">$0.00</p>
                    </div>

                    <div class="orderbuttons mt-auto">
                        <hr class="my-4">
                        <button type="submitorder" class="btn submitorder" id="submitorder" data-toggle="modal"
                            data-target="#orderSubmit" disabled>Submit
                            Order</button>
                        <button type="cancelorder" class="btn cancelorder" id="cancelbutton" data-toggle="modal"
                            data-target="#orderCancel">Cancel Order</button>
                        <p class="my-4"></p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.4.1.js "></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js "></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js "></script>
    <script src="lib\pixi.js"></script>
    <!-- Main Javascript -->
    <script src="main.js" type="text/javascript"></script>
    <script src="createPizza.js" type="text/javascript"></script>

</body>

</html>