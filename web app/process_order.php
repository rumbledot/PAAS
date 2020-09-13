<?php

$names = array();
$quantities = array();
$topID = array();
$pizzas = array();
$lastOrderID = 0;
$customer = "";

$confirmedName = "";
$confirmedStatus = "";
$bases = array();
$sauces = array();
$t1s = array();
$t2s = array();
$t3s = array();
$t4s = array();
$t5s = array();
$t6s = array();
$statuses = array();
$conn = new mysqli("localhost", "abrahamkurnanto_bram", "jazzJYU809", "abrahamkurnanto_spacepizza");
// $conn = new mysqli("10.140.155.18:3306", "alien", "Passw0rd", "paasOne");
// $conn = new mysqli("spacepizza.ceybgcu06f5n.us-west-2.rds.amazonaws.com:3306", "alien", "Passw0rd", "spacepizza");

// ---------------------------- Confirmation page PHP ---------------------------------//

// get order from DB in confirmation page 
if (isset($_GET['orderID'])) {
    $id = $_GET['orderID'];
    $result = $conn->query("SELECT * FROM orders WHERE id = '$id';") or die($conn->error);
    foreach ($result as $row) {
        $confirmedName = $row['name'];
        $confirmedStatus = $row['order_status'];
    }

    $result = $conn->query("SELECT * FROM pizzas WHERE order_id = '$id';") or die($conn->error);
    foreach ($result as $row) {

        $ID = $row['base'];
        $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
        foreach ($result as $r) {
            array_push($bases, $r['name']);
        }
        $ID = $row['sauce'];
        $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
        foreach ($result as $r) {
            array_push($sauces, $r['name']);
        }
        $ID = $row['t1'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t1s, $r['name']);
            }
        } else {
            array_push($t1s, "");
        }
        $ID = $row['t2'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t2s, $r['name']);
            }
        } else {
            array_push($t2s, "");
        }
        $ID = $row['t3'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t3s, $r['name']);
            }
        } else {
            array_push($t3s, "");
        }
        $ID = $row['t4'];
        if ($ID != 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t4s, $r['name']);
            }
        } else {
            array_push($t4s, "");
        }
        $ID = $row['t5'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t5s, $r['name']);
            }
        } else {
            array_push($t5s, "");
        }
        $ID = $row['t6'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t6s, $r['name']);
            }
        } else {
            array_push($t6s, "");
        }
    }
}

// this wll retrieve confirmed pizzas order
if (isset($_POST['pizzasPHP'])) {

    $order = json_decode($_POST['pizzasPHP']);
    $customer = $order->name;
    $pizzas = $order->pizzas;

    $stmt = $conn->prepare("INSERT INTO orders (name, order_status) VALUES (?, 'queued');") or die($conn->error);
    $stmt->bind_param("s", $customer);
    $stmt->execute();
    $lastOrderID = $conn->insert_id;

    foreach ($pizzas as $pizza) {
        $tp = array(99, 99, 99, 99, 99, 99, 99, 99);
        $ind = 0;

        $listofingredients = $pizza->ingredientsName;
        foreach ($listofingredients as $in) {
            $tp[$ind] = $in;
            $ind++;
        }

        $stmt = $conn->prepare("INSERT INTO pizzas (order_id, base, sauce, t1, t2, t3, t4, t5, t6, status) VALUES (?, 1, 2, ?, ?, ?, ?, ?, ?, 'queued');") or die($conn->error);
        $stmt->bind_param("iiiiiii", $lastOrderID, $tp[2], $tp[3], $tp[4], $tp[5], $tp[6], $tp[7]);
        $stmt->execute();
    }

    echo $lastOrderID;
}

// get order details from DB use in conjunction with PizzaPHP
if (isset($_GET['populateOrder'])) {
    $id = $_GET['populateOrder'];

    $result = $conn->query("SELECT * FROM pizzas WHERE order_id = '$id';") or die($conn->error);
    foreach ($result as $row) {

        $ID = $row['base'];
        $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
        foreach ($result as $r) {
            array_push($bases, $r['name']);
        }
        $ID = $row['sauce'];
        $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
        foreach ($result as $r) {
            array_push($sauces, $r['name']);
        }
        $ID = $row['t1'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t1s, $r['name']);
            }
        } else {
            array_push($t1s, "");
        }
        $ID = $row['t2'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t2s, $r['name']);
            }
        } else {
            array_push($t2s, "");
        }
        $ID = $row['t3'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t3s, $r['name']);
            }
        } else {
            array_push($t3s, "");
        }
        $ID = $row['t4'];
        if ($ID != 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t4s, $r['name']);
            }
        } else {
            array_push($t4s, "");
        }
        $ID = $row['t5'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t5s, $r['name']);
            }
        } else {
            array_push($t5s, "");
        }
        $ID = $row['t6'];
        if ($ID <> 99) {
            $result = $conn->query("SELECT name FROM ingredients WHERE id = $ID;") or die($conn->error);
            foreach ($result as $r) {
                array_push($t6s, $r['name']);
            }
        } else {
            array_push($t6s, "");
        }
    }

    $response['id'] = $id;
    $response['bases'] = $bases;
    $response['sauces'] = $sauces;
    $response['t1'] = $t1s;
    $response['t2'] = $t2s;
    $response['t3'] = $t3s;
    $response['t4'] = $t4s;
    $response['t5'] = $t5s;
    $response['t6'] = $t6s;

    echo json_encode($response);
}

// use to get order status every 5 minutes
if (isset($_GET['updateStatus'])) {
    $id = $_GET['updateStatus'];
    $result = $conn->query("SELECT order_status FROM orders WHERE id = '$id';") or die($conn->error);
    foreach ($result as $row) {
        $confirmedStatus = $row['order_status'];
    }
    echo json_encode($confirmedStatus);
}

// ---------------------------- Create Pizza page PHP ---------------------------------//

// check for ingredients amount and if has more than 5 grab it
if (isset($_GET['checkIngredients'])) {
    $name = $_GET['checkIngredients'];
    $available = 0;
    $result = $conn->query("SELECT * FROM ingredients WHERE name = '$name';") or die($conn->error);

    if (mysqli_num_rows($result) >= 1) {
        foreach ($result as $row) {
            $available = $row['quantity'];
        }
    }

    if ($available >= 5) {
        $query = $conn->prepare("UPDATE ingredients SET quantity = quantity - 1 WHERE name = ?");
        $query->bind_param("s", $name);
        $query->execute();
    }
    echo json_encode($available);
}

if (isset($_GET['justCheckIngredients'])) {
    $name = $_GET['justCheckIngredients'];
    $available = 0;
    $result = $conn->query("SELECT * FROM ingredients WHERE name = '$name';") or die($conn->error);

    if (mysqli_num_rows($result) >= 1) {
        foreach ($result as $row) {
            $available = $row['quantity'];
        }
    }
    echo json_encode($available);
}

// this will return ingredients when customer click remove pizza from order
if (isset($_GET['returnIngredients'])) {
    $name = $_GET['returnIngredients'];
    $query = $conn->prepare("UPDATE ingredients SET quantity = quantity + 1 WHERE name = ?");
    $query->bind_param("s", $name);
    $query->execute();
    echo json_encode($name);
}

// use by rePopulate function
if (isset($_GET['refreshType'])) {
    $type = $_GET['refreshType'];
    $result = $conn->query("SELECT id FROM ingredients WHERE type ='$type';") or die($conn->error);
    if (mysqli_num_rows($result) >= 1) {
        while ($row = mysqli_fetch_array($result)) {
            array_push($topID, $row[0]);
        }
    }
    $result = $conn->query("SELECT name FROM ingredients WHERE type ='$type';") or die($conn->error);
    if (mysqli_num_rows($result) >= 1) {
        while ($row = mysqli_fetch_array($result)) {
            array_push($names, $row[0]);
        }
    }
    $result = $conn->query("SELECT quantity FROM ingredients WHERE type ='$type';") or die($conn->error);
    if (mysqli_num_rows($result) >= 1) {
        while ($row = mysqli_fetch_array($result)) {
            array_push($quantities, $row[0]);
        }
    }
    $response['name'] = $names;
    $response['quantity'] = $quantities;
    $response['topID'] = $topID;

    echo json_encode($response);
}