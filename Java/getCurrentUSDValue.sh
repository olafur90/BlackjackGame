#!bin/bash
currentValue=$(curl 'https://chainz.cryptoid.info/smly/api.dws?q=ticker.usd')
echo $currentValue
