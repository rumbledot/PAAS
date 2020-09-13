<?php
session_start();
$someID = $_GET['orderID'];

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

    <!-- Confirmation Main Content -->
    <section class="confirmation-page-intro">
        <div class=" container">
            <div class="row">
                <div class="col-xl-8">
                    <h1 class="welcome"><?php echo $confirmedName ?>,</h1>
                    <h3 class="welcome">Thanks for your order!</h3>
                    <hr class="my-2">
                    <p class="lead">Your order confirmation number is: <?php echo $id ?></h2>
                        <hr class="bg-light">
                        <h4>Order Summary</h4>
                        <hr class="my-3">
                        <div class="orderitemslist" id="orderlist">
                            <?php
                            $count = count($bases);
                            for ($i = 0; $i < $count; $i++) { ?>
                            <div>
                                <h5>Pizza <?php echo $i + 1 ?></h5>
                                <hr class="my-2">
                                <p><?php echo $bases[$i]; ?>
                                    <?php echo $sauces[$i]; ?>
                                    <?php echo $t1s[$i]; ?>
                                    <?php echo $t2s[$i]; ?>
                                    <?php echo $t3s[$i]; ?>
                                    <?php echo $t4s[$i]; ?>
                                    <?php echo $t5s[$i]; ?>
                                    <?php echo $t6s[$i]; ?>
                                </p>
                                <hr class="my-2">
                            </div>
                            <?php
                            }
                            ?>
                        </div>
                </div>
                <div class="col-xl-4 border-top border-bottom rounded bg-dark">
                    <p class="my-4"></p>
                    <h2>Order Status:</h2>
                    <p class="my-4"></p>
                    <div class="">
                        <h3 id="orderstatus">Queued</h3>
                    </div>
                    <hr class="my-4">
                    <p class="lead">Your order will be ready for collection in 20 minutes or your pizza is on us.*</p>
                    <div class="d-flex justify-content-center">
                        <h5 class="display__time-left"></h5>
                        <h5 class="finished"></h5>
                    </div>
                    <p class="text-muted">*For orders with 3 pizzas or less.</p>
                    <p class="my-4"></p>
                </div>
            </div>
        </div>
    </section>
    <p style="visibility: hidden" id="orderNum"><?php echo $id ?></p>
    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.4.1.js "></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js "></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js "></script>
    <!-- Main Javascript -->
    <script src="main.js" type="text/javascript"></script>
    <script src="confirmationPage.js" type="text/javascript"></script>

</body>

</html>