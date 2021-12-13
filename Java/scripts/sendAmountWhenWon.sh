#!/bin/bash

betAmount=$1

smileycoin-cli sendtoaddress BKeXkDQ25EYdqJ3MTyS7M4ujoDnpVEwu1h $betAmount


# $TX verður UTXO þegar ég finn útúr því
# $Vout ---------------||---------------
# $betAmount
# $amountBackToPlayer  --- Fyrir afganginn af UTXO

#tx=$(smileycoin-cli createrawtransaction '[{"txid":'$TX',"vout:"'$vout'}] {"BKeXkDQ25EYdqJ3MTyS7M4ujoDnpVEwu1h":'$betAmount',"BMi44PNRYzvhxRnHeB5Y5aAVprH1g3gmSW":'$amountBackToPlayer',"data":"99"}')
#signedTx=$(smileycoin-cli signrawtransaction $tx)
#smileycoin-cli sendrawtransaction $signedTx


