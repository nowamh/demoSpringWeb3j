// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

library InvestmentLib {
    
    struct Investment {
        uint256 id;
        address payable project;
        address payable investor;
        uint256 amount;
        InvestmentState state;
    }

    enum InvestmentState {
        PENDING,
        PAYED,
        CANCELED,
        FINISHED
    }

    function setInvestment(
        uint _id, 
        address payable _project,
        address payable _investor,
        uint256 _amount,
        InvestmentState _state
    ) 
        public
        pure
        returns(Investment memory) {
            return Investment({
                id: _id,
                project: _project,
                investor: _investor,
                amount: _amount,
                state: _state
            });
    }

    function setInvestmentState(
        Investment memory _investment , 
        InvestmentState _state
    )
        public
        pure {
        _investment.state =_state;
    }

    struct Investor {
        string companyName;
    }
}
