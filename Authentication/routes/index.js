const express = require('express')
const crypto = require('crypto')
const jwt = require("jsonwebtoken")

const router = express.Router()
const secret = "TopSecret"
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
        var token = jwt.sign(user, secret, {expiresIn: 60 * 60 * 24})
        res.json({Token:token})
    }
    res.json({error:"Failed to authenticat"})
})

router.get('/verify', async (req, res) => {
    var token = req.get('oopstoken')
    try {
        var decoded = jwt.verify(token, secret)
        var user = await fetch(baseURL+'/users/'+decoded.username).then((response) => {return response.json()})
        if(user.pwd == decoded.pwd && user.privilege_id == decoded.privilege_id) {
            res.sendStatus(401)
        }
        res.sendStatus(200)
    } catch(err) {
        switch (err.name) {
            case JsonWebTokenError:
                res.json({erorr:"Failed to authenticate"}).status(401)
                break;
            
            case TokenExpiredError:
                res.json({error:"Token expired"}).status(401)
                break;
            
            default:
                res.json({erorr:"Failed to authenticate"}).status(401)
                break;
        }
    }
})

router.get('/login', async (req, res) => {
    var response = await fetch("http://localhost:80/users/test").then((response) => {
        return response.json()
    })

    res.json(response)
})

module.exports = router