express = require('express')
const https = require('https')
const fs = require('fs')

var key = fs.readFileSync('certs/key.pem')
var cert = fs.readFileSync('certs/cert.pem')
var options = {
    key: key,
    cert: cert
}

const port = 3000

const indexRouter = require('./routes/index')

const app = express()

app.use('/', indexRouter)

var server = https.createServer(options, app)

server.listen(port, () => {
    console.log(`Started server on port ${port}`)
})