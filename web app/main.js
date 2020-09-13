// -----------------------Toggle Menu Javascript--------------------------//

//Expand/collapse mobile nav on icon click
$(document).ready(function() {
    $('.menu-toggle').click(function() {
        $('nav').toggleClass('active');
        $('.fa-times').toggleClass('swap');
        $('.fa-bars').toggleClass('swap');
    });

    var requestAnimationFrame =
        window.requestAnimationFrame ||
        window.mozRequestAnimationFrame ||
        window.webkitRequestAnimationFrame ||
        window.msRequestAnimationFrame ||
        function(callback) {
            window.setTimeout(callback, 1000 / 160);
        };
    window.requestAnimationFrame = requestAnimationFrame;
});


// ------------------Create Pizza Modal Javascript-------------------//

$('#myModal').on('shown.bs.modal', function() {
    $('#myInput').trigger('focus')
})

// -----------------------Galaxy Canvas Javascript--------------------------//

var background = document.getElementById('bgcanvas'),
    bgCtx = background.getContext('2d'),
    width = window.innerWidth,
    height = window.innerHeight;

height < 400 ? (height = 400) : height;

background.width = width;
background.height = height;

function Star(options) {
    this.size = Math.random() * 2;
    this.speed = Math.random() * 0.05;
    this.x = options.x;
    this.y = options.y;
}

Star.prototype.reset = function() {
    this.size = Math.random() * 2;
    this.speed = Math.random() * 0.05;
    this.x = width;
    this.y = Math.random() * height;
};

Star.prototype.update = function() {
    this.x -= this.speed;
    if (this.x < 0) {
        this.reset();
    } else {
        bgCtx.fillRect(this.x, this.y, this.size, this.size);
    }
};

function ShootingStar() {
    this.reset();
}

ShootingStar.prototype.reset = function() {
    this.x = Math.random() * width;
    this.y = 0;

    this.len = Math.random() * 80 + 10;
    this.speed = Math.random() * 10 + 6;
    this.size = Math.random() * 1 + 0.1;
    // this is used so the shooting stars arent constant
    this.waitTime = new Date().getTime() + Math.random() * 3000 + 500;
    this.active = false;
};

ShootingStar.prototype.update = function() {
    if (this.active) {
        this.x -= this.speed;
        this.y += this.speed;
        if (this.x < 0 || this.y >= height) {
            this.reset();
        } else {
            bgCtx.lineWidth = this.size;
            bgCtx.beginPath();
            bgCtx.moveTo(this.x, this.y);
            bgCtx.lineTo(this.x + this.len, this.y - this.len);
            bgCtx.stroke();
        }
    } else {
        if (this.waitTime < new Date().getTime()) {
            this.active = true;
        }
    }
};

var entities = [];

for (var i = 0; i < height; i++) {
    entities.push(
        new Star({
            x: Math.random() * width,
            y: Math.random() * height
        })
    );
}
entities.push(new ShootingStar());

function animate() {
    bgCtx.fillStyle = 'black';
    bgCtx.fillRect(0, 0, width, height);
    bgCtx.fillStyle = '#FFFFFF';
    bgCtx.strokeStyle = '#ffffff';
    var entLn = entities.length;
    while (entLn--) {
        entities[entLn].update();
    }
    requestAnimationFrame(animate);
}
animate();

// -----------------------Next button Javascript--------------------------//
// we can check for name and phone and store them in DB
function gotoNextPage() {
    var name = document.getElementById('cust_name').value;
    var phone = document.getElementById('cust_phone').value;
    var human = document.getElementById('humanCheck1').checked;
    if (human == true) {
        if (name != "") {
            window.location.href = "createPizza.php?name=" + name;
        } else {
            alert("Please enter a valid name.");
        }
    } else {
        alert("Please prove you're human.");
    }
}

$("#inputTextBox").keypress(function(event) {
    var inputValue = event.which;
    // allow letters and whitespaces only.
    if (!(inputValue >= 65 && inputValue <= 120) && (inputValue != 32 && inputValue != 0)) {
        event.preventDefault();
    }
});