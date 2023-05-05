import {AccountOperation} from "./operation.model";

export interface BankAccount {
  balance:     number;
  type:        string;
  currentPage: number;
  totalPages:  number;
  size:        number;
  operations:  AccountOperation[];
  id:          string;
  rib:         string;
}
