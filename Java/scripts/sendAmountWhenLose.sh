#!/bin/bash

amountToHouse=$1
amountToCharity=$2

smileycoin-cli sendtoaddress BKeXkDQ25EYdqJ3MTyS7M4ujoDnpVEwu1h $amountToHouse
smileycoin-cli sendtoaddress BLcESPzsJp7bcFvuiCXvu9qB54h4vtgky7 $amountToCharity

