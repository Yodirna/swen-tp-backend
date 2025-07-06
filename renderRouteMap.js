const puppeteer = require('puppeteer');
const fs = require('fs');

(async () => {
    const routeGeoJson = process.argv[2]; // JSON String of coordinates
    const outPath = process.argv[3]; // output PNG path

    const htmlContent = `
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="utf-8" />
        <title>Route Map</title>
        <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css"/>
        <style> #map { width:600px; height:400px; } body {margin:0;} </style>
    </head>
    <body>
        <div id="map"></div>
        <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
        <script>
            var map = L.map('map').setView([0, 0], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: 'Map data © OpenStreetMap contributors'
            }).addTo(map);
            var coords = ${routeGeoJson};
            console.log(coords);
            var latlngs = coords.map(pt => [pt[1], pt[0]]);
            var polyline = L.polyline(latlngs, {color:'blue'}).addTo(map);
            map.fitBounds(polyline.getBounds());
        </script>
    </body>
    </html>`;

    // Launch browser
    const browser = await puppeteer.launch();
    const page = await browser.newPage();

    await page.setContent(htmlContent, {waitUntil: 'networkidle0'});
    await page.waitForSelector('#map');
    await new Promise(resolve => setTimeout(resolve, 500)); // let leaflet finish


    const mapHandle = await page.$('#map');
    await mapHandle.screenshot({path: outPath});

    await browser.close();
})();
