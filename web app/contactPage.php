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

    <!-- Navigation ** includes mobile navigation ** -->
    <section class="navigation">
        <header>
            <div class="logo">
                <a href="index.html"><img src="img/pizza.png" alt="PAAS Co Logo"></a>
            </div>
            <nav id="main-nav" class="nav">
                <ul>
                    <li><a href="index.html">Home</a></li>
                    <li><a href="setUpOrder.php">Order</a></li>
                    <li><a href="aboutPage.php">About</a></li>
                    <li><a class="active" href="contactPage.php">Contact</a></li>
                </ul>
            </nav>
            <div class="menu-toggle"><i class="fas fa-bars"></i><i class="fas fa-times swap"></i></div>
        </header>
    </section>

    <!-- Background Galaxy Canvas -->
    <canvas id="bgcanvas"></canvas>

    <!-- Main Content -->
    <section class="home-page-intro">
        <div class="container shadow">
            <div class="row row justify-content-center">
                <div class="col-sm-8 col-l-6">
                    <h1 class="welcome">SORRY EARTHLINGS!</h1>
                    <hr class="my-4">
                    <h4 class="subtitle">WE ARE STILL BAKING THIS PAGE</h4>
                    <hr class="my-2">
                    <p class="front" style="font-size:30px">BUT</p>
                    <hr class="my-2">
                    <p class="front">YOU CAN STILL PLACE YOUR ORDER..</p>
                    <hr class="my-4">
                    <a class="btn btn-lg " href="setUpOrder.php " role="button ">Order Here!</a>
                </div>
            </div>
        </div>
    </section>

    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.1.1.min.js "></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js "></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js "></script>
    <!-- Main Javascript -->
    <script src="main.js " type="text/javascript "></script>

</body>

</html>