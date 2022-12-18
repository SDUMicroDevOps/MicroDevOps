import { v4 as uuidv4 } from 'uuid';

class SolverManager {
    constructor(name) {
        this.id = uuidv4();
        this.name = name;
        this.available = true;
        this.selected = false;
        this.running = false;
    }
  }
  
  export default SolverManager;