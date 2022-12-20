const express = require('express')
const cors = require('cors')
/*const https = require('https')
const fs = require('fs')

var key = fs.readFileSync('certs/key.pem')
var cert = fs.readFileSync('certs/cert.pem')
var options = {
    key: key,
    cert: cert
}*/

const port = 3000

const indexRouter = require('./routes/index')

const app = express()

app.use(express.json())
app.use(cors({origin: true, credentials: true}));
app.use('/', indexRouter)

//var server = https.createServer(options, app)

app.listen(port, () => {
    console.log(`Started server on port ${port}`)
})
