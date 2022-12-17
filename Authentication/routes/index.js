const express = require('express')
const router = express.Router()
const crypto = require('crypto')
const secret = "TopSecret"
const jwt = require("jsonwebtoken")
const baseURL = 'http://' + process.env.DATABASE_SERVICE + ':' + process.env.DATABASE_PORT

router.get('/', (req, res) => {
    res.send('Default endpoint for auth service')
})

router.put('/login', async (req, res) => {
    var username = req.body.Username
    var pwdhash = crypto.createHash("sha256").update(req.body.Password).digest("hex")

    //check if user exist and has correct pwd
    var user = await fetch(baseURL+'/users/'+username).then((response) => {return response.json()})

    if(pwdhash == user.pwd) { 
        var token = jwt.sign({
            Username:username,
            Password:pwdhash
        }, secret, {expiresIn: 60 * 60 * 24})
        res.json({Token:token})
    }
    res.json({error:"Failed to authenticat"})
})

router.get('/verify', (req, res) => {
    var token = req.get('oopstoken')
    try {
        var decoded = jwt.verify(token, secret)
        res.sendStatus(200)
    } catch(err) {

    }
})

router.get('/login', async (req, res) => {
    var response = await fetch("http://localhost:80/users/test").then((response) => {
        return response.json()
    })

    res.json(response)
})

module.exports = router