import BasicUserManager from './BasicUserManager'
class EndUserManager extends BasicUserManager{
    constructor(username){
        super(username);
        this.maxvcpus = 0;
        this.runningSolvers = [];
    }
}

export default EndUserManager;