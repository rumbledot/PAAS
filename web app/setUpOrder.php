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
                <a href="index.html"><img src="img/pizza.png" alt="PAAS Co
                            Logo"></a>
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

    <!-- Set Up Order Main Content -->
    <section class="setup-page-intro">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <h1 class="welcome">New Order</h1>
                    <hr class="my-4">
                    <form action="setUpOrder.php" method="post">
                        <div class="form-group">
                            <label for="exampleInputName1">Order Name</label>
                            <input type="text" class="form-control" name="cust_name" id="cust_name"
                                aria-describedby="nameHelp" placeholder="Enter Order Name" maxlength="30">
                            <small id="nameHelp" class="form-text
                                    text-muted">Overlord is fine with us. Max 30 characters.</small>
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPhone1">Phone Number*</label>
                            <input type="text" class="form-control" name="cust_phone" id="cust_phone"
                                aria-describedby="phoneHelp" placeholder="Enter Phone Number">
                            <small id="phoneHelp" class="form-text text-muted">*Optional</small>

                        </div>
                        <hr class="my-3">
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" id="humanCheck1">
                            <label class="form-check-label" for="exampleCheck1">Check me to prove you
                                are human...</label>
                        </div>
                        <hr class="my-3">
                    </form>
                    <button class="btn" type="submit" value="next" name="loginBtn"
                        onclick="gotoNextPage()">Next</button>
                </div>
                <div class="d-none col-md-6">

                </div>
            </div>
        </div>
    </section>



    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <!-- <script src="https://code.jquery.com/jquery-3.4.1.js "></script> -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <!-- Main Javascript -->
    <script src="main.js" type="text/javascript"></script>

</body>

</html>