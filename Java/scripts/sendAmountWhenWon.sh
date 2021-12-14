#!/bin/bash

logfile='/home/olafur/Documents/Skóli/Rafmyntir/Lokaverkefni/Java/scripts/logfile.txt'

if [ ! -f $logfile ];
then
touch $logfile;
fi

betAmount=$1
intBetAmount=${betAmount%.*}

bias=10
UTXOAmount=0
intAmount=0
counter=0

echo "START WHILE"
while [ $intAmount -lt $intBetAmount ]
do
  UTXOAmount=$(echo `smileycoin-cli listunspent | jq '.['$counter'] | .amount'`)
  echo $UTXOAmount
  intAmount=${UTXOAmount%.*}
  counter=$(expr $counter + 1)
done
echo "END WHILE"
utxoAmount=$(echo `smileycoin-cli listunspent | jq '.['$counter'] | .amount'`)

TX=$(echo `smileycoin-cli listunspent | jq '.['$(expr $counter - 1)'] | .txid'`)
vout=$(echo `smileycoin-cli listunspent | jq '.['$(expr $counter - 1)'] | .vout'`)
echo "txid: $TX" >> $logfile
echo "vout: $vout" >> $logfile
echo "amount: $intAmount" >> $logfile
echo "Bet amount: $betAmount" >> $logfile


amountBackToPlayer=$(($intAmount-$intBetAmount-$bias))

echo "Amount back to player: $amountBackToPlayer" >> $logfile

txID=$(echo $TX | sed 's/"//g')
#echo $txID
#echo $vout
tx=$(smileycoin-cli createrawtransaction '[{"txid" : "'$txID'","vout" : '$vout'}]' '{"BKeXkDQ25EYdqJ3MTyS7M4ujoDnpVEwu1h" : '$intBetAmount', "BMi44PNRYzvhxRnHeB5Y5aAVprH1g3gmSW" : '$amountBackToPlayer', "data" : "99"}')

echo "txid: $tx" >> $logfile

signedTx=$(smileycoin-cli signrawtransaction $tx | awk '/"hex"/ {print $3}' | sed 's/"//g' | sed 's/,$//')

echo "Signed tx: $signedTx" >> $logfile
#echo $signedTx

smileycoin-cli sendrawtransaction $signedTx true

