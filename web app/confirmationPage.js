var countdown;
const timerDisplay = document.querySelector('.display__time-left');
timer(1200);

function timer(seconds) {
    const now = Date.now();
    //now is in ms so seconds needs to be multiplied by 1000.
    const then = now + seconds * 1000;
    displayTimeLeft(seconds);

    var interval = setInterval(() => {
        const secondsLeft = Math.round((then - Date.now()) / 1000);
        if (secondsLeft < 0) {
            clearInterval(countdown);
            timerDisplay.style.fontSize = "30px";
            timerDisplay.textContent = "Order Ready!";
            return;
        }
        displayTimeLeft(secondsLeft);
    }, 1000);
}

function displayTimeLeft(seconds) {
    const minutes = Math.floor(seconds / 60);
    const remainderSeconds = seconds % 60;
    const display = `${minutes}:${remainderSeconds < 10 ? '0' : ''}${remainderSeconds}`;
    timerDisplay.textContent = display;
}

var confirmedID = document.getElementById('orderNum').innerHTML;

// basic function to call php and get json as result
async function API(url) {
    const response = await fetch("process_order.php" + url, {
        method: 'GET'
    });
    const data = await response.json();
    return data;
}

// displaying confirmed order details
// API("?populateOrder=" + confirmedID).then(data => {
//     console.log(data);
//     var bases = data.bases;
//     var sauces = data.sauces;
//     var t1s = data.t1;
//     var t2s = data.t2;
//     var t3s = data.t3;
//     var t4s = data.t4;
//     var t5s = data.t5;
//     var t6s = data.t6;

//     var list = document.getElementById('orderlist');

//     for (i = 0; i < bases.length; i++) {
//         var div = document.createElement('div');
//         list.appendChild(div);

//         var p = document.createElement('p');
//         p.innerHTML = "Pizza " + (i + 1);
//         div.appendChild(p);

//         var p1 = document.createElement('p');
//         p1.innerHTML = bases[i] + " " + sauces[i] + " " + t1s[i] + " " + t2s[i] + " " + t3s[i] + " " + t4s[i] + " " + t5s[i] + " " + t6s[i];

//         div.appendChild(p1);
//     }
// }).catch(e => {
//     console.log(e);
// });

// update status every 5 minutes
// setInterval(function() {
//     API("?updateStatus=" + confirmedID).then(data => {
//         var updater = document.getElementsByClassName('status');
//         updater.innerHTML = data;
//     });
// }, 60000);

// update order status every 5 secs
setInterval(function() {
    API("?updateStatus=" + confirmedID).then(data => {
        var updater = document.getElementById('orderstatus');
        if (data == 'queued') {
            updater.innerHTML = "Queued";
            updater.style.color = "#ffffff";
        } else if (data == 'started') {
            updater.innerHTML = "Started";
            updater.style.color = "#ff0000";
        } else {
            updater.innerHTML = "Completed";
            updater.style.color = "#00cc00";
            timerDisplay.style.display = 'none';
            let d = document.querySelector('.finished');
            d.style.fontSize = "30px";
            d.innerHTML = 'Order Ready!';

        }
    });
}, 5000);

// called after 20 mins
setTimeout(function() {

}, 1200000);