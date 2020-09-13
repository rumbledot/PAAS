// -----------------------Pizza Maker Javascript--------------------------//
//Create something
stat = true;

// topping object
class toppingItem {
    constructor(layer, name, ID) {
            this.layer = layer;
            this.name = name;
            this.ingredientID = ID;
            this.sprites = [];
        }
        // which layer the topping exist
    getLayer() {
            return this.layer;
        }
        // get topping name
    getName() {
        return this.name;
    }
    getID() {
            return this.ingredientID;
        }
        // adding sprites to topping object
    add(sprite) {
            this.sprites.push(sprite);
        }
        // getting all sprites
    getSprites() {
        return this.sprites;
    }
}

// pizza object
class pizza {
    constructor(ID) {
        this.ID = ID;
        this.ingredients = [];
        this.ingredientsName = [];
    }

    getID() {
        return this.ID;
    }

    setID(ID) {
        this.ID = ID;
    }

    addIngredients(ingredient, ingredientName) {
        this.ingredients.push(ingredient);
        this.ingredientsName.push(ingredientName);
    }

    getIngredients() {
        return this.ingredients;
    }

    getIngredientsName() {
        return this.ingredientsName;
    }
}

// order object
class orderUp {
    constructor(name) {
        this.name = name;
        this.pizzas = [];
    }

    addPizzas(pizzas) {
        this.pizzas = pizzas;
    }

    getPizzas() {
        return this.pizzas;
    }
}

// -----------------------pixi.js setup--------------------------//

//initialise PIXI JS and use canvas with id = pizzamaker
const app = new PIXI.Application({
    view: pizzamaker,
    width: 400,
    height: 400
});

// data vars
var toppings = [];
var toppingsSprite = [];
var toppingCount = 0;
var toppingItems = [];

var orders = [];
var maxPizzas = 10;
var pizzas = [];
var pizzaNum = 0;
var pizzaUnique = 0;

// PIXI JS layers
const baseLayer = new PIXI.Container();
const sauceLayer = new PIXI.Container();
const toppingLayer = new PIXI.Container();
const layers = [];
// append layers to PIXI JS
app.stage.addChild(baseLayer);
app.stage.addChild(sauceLayer);
app.stage.addChild(toppingLayer);
for (let i = 0; i < 6; i++) {
    const t = new PIXI.Container();
    layers.push(t);
    app.stage.addChild(t);
}

var images = [
    'Standard Base',
    'Mozzarella',
    'Camembert',
    'Vegan Mozzarella',
    'Salami',
    'Chicken',
    'Bacon',
    'Vegan Chicken',
    'Spinach',
    'Mushroom',
    'Tomato',
    'Onion',
    'Olives',
    'Jalapeno',
    'Capsicum',
    'Pineapple'
];

// loading images to be use as PIXI JS sprites
for (let a = 0; a < images.length; a++) {
    PIXI.loader.add('img/' + images[a] + '.png');
}
PIXI.loader.load(setup);

// put the pizza base
function setup() {
    let pizzaBase = new PIXI.Sprite(PIXI.loader.resources['img/Standard Base.png'].texture);
    pizzaBase.x = 200;
    pizzaBase.y = 200;
    pizzaBase.width = 400;
    pizzaBase.height = 400;
    pizzaBase.anchor.x = 0.5;
    pizzaBase.anchor.y = 0.5;
    toppingItems.push(pizzaBase);
    baseLayer.addChild(pizzaBase);
}

rePopulate();

// called when user add a pizza to order list
// repopulate checkboxes with updated ingredient amount
function rePopulate() {
    API('?justCheckIngredients=Standard Base').then((baseNum) => {
        API('?justCheckIngredients=Classic Tomato').then((sauceNum) => {
            console.log("check  : " + baseNum + " : " + sauceNum);
            if (baseNum > 4 && sauceNum > 4) {
                if (pizzas.length < maxPizzas) {
                    // re populate ingredient based on its category
                    // populate("Base");
                    // populate("Sauce");

                    populate('Cheese');
                    populate('Meat');
                    populate('Veggies');
                }
            } else {
                alert("Oops! Looks like we have just run out of stock of essential ingredients. Please submit your order as is or try back another day. Sorry for the inconvenience.");
                let save = document.getElementById("addpizza");
                save.disabled = true;
                let inputs = document.getElementsByTagName("input");
                for (let i = 0; i < inputs.length; i++) {
                    if (inputs[i].type == 'checkbox') {
                        inputs[i].disabled = true;
                    }
                }

            }
        });
    });
}

function populate(type) {
    // get Pizza Base div and create
    var div = document.getElementById('div' + type);
    div.innerHTML = 'Refreshing ' + type + '...';

    API('?refreshType=' + type)
        .then((data) => {
            var div = document.getElementById('div' + type);
            div.innerHTML = '';
            names = data.name;
            quantities = data.quantity;
            topID = data.topID;
            for (i = 0; i < names.length; i++) {
                if (quantities[i] >= 5) {
                    var divCBContainer = document.createElement('div');
                    divCBContainer.setAttribute('class', 'form-check form-check-inline ');
                    div.appendChild(divCBContainer);

                    var CB = document.createElement('input');
                    CB.setAttribute('class', 'form-check-input');
                    CB.setAttribute('type', 'checkbox');
                    CB.setAttribute('id', names[i]);
                    CB.setAttribute('onclick', 'addTopping(' + '"' + names[i] + '"' + ',' + topID[i] + ')');


                    // if (type == 'Base' || type == 'Sauce') {
                    //     CB.checked = true;
                    //     CB.disabled = true;
                    // } else {

                    //clearing the checkoxes
                    CB.checked = false;
                    CB.disabled = false;
                    // }

                    divCBContainer.appendChild(CB);

                    var label = document.createElement('label');
                    label.setAttribute('class', 'form-check-label');
                    label.setAttribute('for', 'inlineCheckbox1');
                    // label.setAttribute('onclick', 'addTopping(' + '"' + names[i] + '"' + ',' + topID[i] + ')');
                    label.innerHTML = names[i];
                    divCBContainer.appendChild(label);
                }
            }
        })
        .catch((e) => {
            console.log(e);
        });
}



// checkboxes controller and will create topping sprite based on chosen ingredient
function addTopping(toppingName, ingredientID) {
    let cb = document.getElementById(toppingName);
    selected = cb.checked;
    // if the checkbox is checked and toppings less then 5
    if (selected == true && toppingCount < 6) {
        console.log("entry");
        let newtopping = new toppingItem(toppingCount, toppingName, ingredientID);
        var exist = images.includes(toppingName);
        if (exist == true) {

            topping = new PIXI.Sprite(PIXI.loader.resources['img/' + toppingName + '.png'].texture);
            let x = 25;
            let y = 25;
            topping.x = x;
            topping.y = y;
            topping.width = 350;
            topping.height = 350;
            newtopping.add(topping);
            layers[toppingCount].addChild(topping);

        }
        // record the topping and update allowed topping quantity
        toppings.push(newtopping);
        toppingCount++;
        console.log(toppingCount);
    } else {
        // this will remove ingredient's sprite and remove unchecked ingredient from array
        for (let i = 0; i < toppings.length; i++) {
            if (toppings[i].getName() === toppingName) {
                let toppingLayerNum = toppings[i].getLayer();
                let toppingSprites = toppings[i].getSprites();
                for (let j = 0; j < toppingSprites.length; j++) {
                    layers[toppingLayerNum].removeChild(toppingSprites[j]);
                }
                toppings.splice(i, 1);
            }
        }
        // update allowed topping quantity
        toppingCount--;
    }

    // update all checkboxes checked status
    let allchecks = document.getElementsByTagName('input');
    if (toppingCount >= 6) {
        for (let i = 0; i < allchecks.length; i++) {
            if (allchecks[i].type == 'checkbox' && allchecks[i].checked == false) {
                allchecks[i].disabled = true;
            }
        }
    } else {
        for (let i = 0; i < allchecks.length; i++) {
            if (allchecks[i].type == 'checkbox') {
                allchecks[i].disabled = false;
            }
        }
    }
}

// -----------------------Pizza Ordering methods Javascript--------------------------//

// called when customer add their pizza creation to order list
function addPizza() {
    // create a new pizza object and get the toppings
    let newPizza = new pizza(pizzaUnique);

    newPizza.addIngredients('Standard Base', 1);
    newPizza.addIngredients('Classic Sauce', 2);
    for (let i = 0; i < toppings.length; i++) {
        newPizza.addIngredients(toppings[i].getName(), toppings[i].getID());
        console.log("Add pizza topping " + toppings[i].getName() + " : " + toppings[i].getID());
    }
    // lets check ingredients availability in the DB
    checkIngredients();
    console.log(stat);

    if (stat === true) {
        //clear out topping related vars
        resetPizza();
        toppings = [];
        toppingCount = 0;

        // tracking pizza quantity
        pizzaNum++;
        pizzas.push(newPizza);
        for (let i = 0; i < pizzas.length; i++) {
            pizzas[i].setID(i);
        }

        refreshOrderList();
        rePopulate();
        updateOrderTotal();
        resetChecks();

        if (pizzas.length >= maxPizzas) {
            let savepizza = document.getElementById('addpizza');
            savepizza.disabled = true;
            console.log(pizzas.length);
            alert('You have reached the max online order of 10 pizzas. Please call the store for orders over 10 pizzas.');

            let allchecks = document.getElementsByTagName('input');
            console.log("all checks");
            for (let i = 0; i < allchecks.length; i++) {
                if (allchecks[i].type == 'checkbox') {
                    allchecks[i].disabled = true;
                }
            }
        }

        if ((document.getElementById('submitorder').disabled = true)) {
            document.getElementById('submitorder').disabled = false;
        }
    } else {
        console.log("Pizza can't be added to order, ingredients out of stock.");
    }
}

function updateOrderTotal() {
    var orderTotal = document.getElementById('ordertotal');
    orderTotal.innerHTML = '$' + pizzaNum * 20 + '.00';
}

function refreshOrderList() {
    let orderListDiv = document.getElementById('orderlist');
    orderListDiv.innerHTML = '';

    for (let k = 0; k < pizzas.length; k++) {
        //display the newly added pizza to the order list
        //get new pizza ingredients
        let currentPizza = pizzas[k];
        pizzas[k].setID(k);

        let pizzaDiv = document.createElement('div');
        orderListDiv.appendChild(pizzaDiv);
        let ptitle = document.createElement('h3');
        ptitle.innerText = 'Pizza ' + (k + 1);
        pizzaDiv.appendChild(ptitle);

        //set a delete pizza button
        let b = document.createElement('button');
        b.setAttribute('id', k);
        b.setAttribute('class', 'deleteButton btn');
        b.style.margin = '0';
        b.style.display = 'inline-block';
        // b.style.padding = '2px';
        b.innerText = 'X';
        //delete pizza button listener
        b.addEventListener('click', function() {
            resetPizza();
            toppings = [];
            toppingCount = 0;

            pizzaNum--;
            returnIngredients(currentPizza);

            for (let j = 0; j < pizzas.length; j++) {
                if (pizzas[j].getID() == this.id) {
                    pizzas.splice(j, 1);
                }
            }
            toppingCount = 0;
            toppings = [];
            refreshOrderList();

            this.parentElement.innerHTML = '';
            if (pizzaNum < 1) {
                document.getElementById('submitorder').disabled = true;
            }

            refreshOrderList();
            rePopulate();
            updateOrderTotal();
            resetChecks();

            if (pizzas.length < maxPizzas) {
                let savepizza = document.getElementById('addpizza');
                savepizza.disabled = false;

                let allchecks = document.getElementsByTagName('input');
                for (let i = 0; i < allchecks.length; i++) {
                    if (allchecks[i].type == 'checkbox') {
                        allchecks[i].disabled = true;
                    }
                }

            }
        });
        pizzaDiv.appendChild(b);

        // display the pizza's ingredients on the list
        let newPizzaIngreds = pizzas[k].getIngredients();
        for (let j = 0; j < newPizzaIngreds.length; j++) {
            console.log('Ingredient : ' + newPizzaIngreds[j]);
            let p = document.createElement('p');
            p.innerText = newPizzaIngreds[j];
            pizzaDiv.appendChild(p);
        }
    }
}

// check for base and sauce quantity in DB every 1 sec
// setInterval(function() {
//     API('?justCheckIngredients=Standard Base').then((baseNum) => {
//         API('?justCheckIngredients=Classic Tomato').then((sauceNum) => {
//             let savepizza = document.getElementById('addpizza');
//             if (baseNum < 5 || sauceNum < 5) {
//                 if (savepizza.disabled == false) {
//                     savepizza.disabled = true;
//                     if (baseNum < 5) {
//                         alert(
//                             'Oops! We ran out of bases. Either submit your order as is or try back again another time. Sorry for the inconvenience.'
//                         );
//                     } else if (sauceNum < 5) {
//                         alert(
//                             'Oops! We ran out of sauces. Either submit your order as is or try back again another time. Sorry for the inconvenience.'
//                         );
//                     }
//                 }
//             } else if (baseNum >= 5 && sauceNum >= 5 && pizzas.length < maxPizzas && toppingCount < 6) {
//                 console.log("interval" + toppingCount);
//                 savepizza.disabled = false;
//                 let allchecks = document.getElementsByTagName('input');
//                 for (let i = 0; i < allchecks.length; i++) {
//                     if (allchecks[i].type == 'checkbox') {
//                         allchecks[i].disabled = false;
//                     }
//                 }
//             }
//         });
//     });
// }, 1000);

// this will check ingredients of submitted pizza and subtract DB ingredient's quantity value
function checkIngredients() {
    // checking Base
    stat = true;
    fetch('process_order.php?checkIngredients=Standard Base').then((res) => res.json()).then((data) => {
        let ingredientQuantity = data;
        if (ingredientQuantity < 5) {
            console.log('not enough base');
            stat = false;
        }
    });
    // checking Sauce
    fetch('process_order.php?checkIngredients=Classic Tomato').then((res) => res.json()).then((data) => {
        let ingredientQuantity = data;
        if (ingredientQuantity < 5) {
            console.log('not enough sauce');
            stat = false;
        }
    });
    // checking other topping types
    for (i = 0; i < toppings.length; i++) {
        let ingredientName = toppings[i].getName();
        console.log('Check for ingredient: ' + ingredientName);
        // send instruction to php and return result in json
        fetch('process_order.php?checkIngredients=' + ingredientName).then((res) => res.json()).then((data) => {
            let ingredientQuantity = data;
            if (ingredientQuantity < 5) {
                console.log('not enough ' + ingredientName);
                stat = false;
            }
        });
    }
}

// this will return ingredients back to DB when user cancel a pizza order
function returnIngredients(pizza) {
    pizzaToReturn = pizza;
    let returnThese = pizzaToReturn.getIngredients();
    // returning Base
    // fetch('process_order.php?returnIngredients=Standard Base').then((res) => res.json()).then((data) => {
    //     let ingredientQuantity = data;
    // });
    // returning Sauce
    fetch('process_order.php?returnIngredients=Classic Tomato').then((res) => res.json()).then((data) => {
        let ingredientQuantity = data;
    });
    // returning other topping types
    for (i = 0; i < returnThese.length; i++) {
        let ingredientName = returnThese[i];
        // send instruction to php and return result in json
        fetch('process_order.php?returnIngredients=' + ingredientName).then((res) => res.json()).then((data) => {
            let ingredientQuantity = data;
        });
    }
}


// reset checkboxes status
function resetChecks() {
    if (pizzas.length < maxPizzas) {
        let allchecks = document.getElementsByTagName('input');
        for (let i = 0; i < allchecks.length; i++) {
            if (allchecks[i].type == 'checkbox') {
                allchecks[i].checked = false;
                allchecks[i].disabled = false;
            }
        }
    }
}
// reset pixi.js canvas
function resetPizza() {
    for (let i = 0; i < toppings.length; i++) {
        let toppingLayerNum = toppings[i].getLayer();
        let toppingSprites = toppings[i].getSprites();
        for (let j = 0; j < toppingSprites.length; j++) {
            layers[toppingLayerNum].removeChild(toppingSprites[j]);
        }
    }
}

// -----------------------Order methods Javascript--------------------------//

$('#orderSubmit').on('show.bs.modal', function(e) {
    var list = document.getElementById('pizzasList');
    list.innerHTML = '';

    var finalAmount = document.getElementById('finalAmount');
    finalAmount.innerHTML = 'Order Total: $' + pizzas.length * 20 + '.00';

    for (let i = 0; i < pizzas.length; i++) {
        var list = document.getElementById('pizzasList');
        //display each pizza in the orderSubmit modal
        console.log(pizzas[i].getID());
        let pizzaDiv = document.createElement('li');

        let ptitle = document.createElement('h6');
        ptitle.innerText = 'Pizza ' + (i + 1);
        pizzaDiv.appendChild(ptitle);

        // display the pizza's ingredients on the list
        let newPizzaIngreds = pizzas[i].getIngredients();
        for (let j = 0; j < newPizzaIngreds.length; j++) {
            console.log(newPizzaIngreds[j]);
            let p = document.createElement('p');
            p.innerText = newPizzaIngreds[j];
            pizzaDiv.appendChild(p);
        }

        list.appendChild(pizzaDiv);
    }
});

function confirmOrder() {
    let customerName = document.getElementById('customerName').innerHTML;
    let order = new orderUp(customerName);
    order.addPizzas(pizzas);

    let tosent = JSON.stringify(order);
    $.ajax({
        url: 'process_order.php',
        method: 'post',
        data: {
            pizzasPHP: tosent
        },
        success: function(res) {
            window.location.href = 'confirmationPage.php?orderID=' + res;
        }
    });
}

function cancelOrder() {
    if (pizzas.length > 0) {
        for (let k = 0; k < pizzas.length; k++) {
            returnIngredients(pizzas[k]);
        }
    }
    pizzas = [];
    pizzaNum = 0;
    toppings = 0;
    toppingCount = 0;
    location.href = 'index.html';
}

// -----------------------Populate form's ingredient checkboxes--------------------------//
async function API(url) {
    const response = await fetch('process_order.php' + url, {
        method: 'GET'
    });
    const data = await response.json();
    return data;
}



// function populateSauce() {
//     // get Pizza Base div and create
//     var div = document.getElementById('divSauce');
//     div.innerHTML = '';

//     API('?refreshType=Sauce').then((data) => {
//         names = data.name;
//         quantities = data.quantity;
//         for (i = 0; i < names.length; i++) {
//             if (quantities[i] >= 5) {
//                 var divCBContainer = document.createElement('div');
//                 divCBContainer.setAttribute('class', 'form-check form-check-inline');
//                 div.appendChild(divCBContainer);

//                 var CB = document.createElement('input');
//                 CB.setAttribute('class', 'form-check-input');
//                 CB.setAttribute('type', 'checkbox');
//                 CB.setAttribute('id', 'base');
//                 CB.setAttribute('checked', 'false');
//                 divCBContainer.appendChild(CB);

//                 var label = document.createElement('label');
//                 label.setAttribute('class', 'form-check-label');
//                 label.setAttribute('for', 'inlineCheckbox1');
//                 label.innerHTML = names[i] + '(' + quantities[i] + ')';
//                 divCBContainer.appendChild(label);
//             }
//         }
//     });
// }

// function populateCheese() {}

// function populateMeat() {}

// function populateVeggie() {}