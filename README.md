# BlackjackGame

Linkur á bottann sem keyrir á annari tölvu
https://github.com/olafur90/BlackjackBot.git

Þurfti að gefa java leyfi til þess að búa til og vinna með skrár í gegnum forritið. Ég hef ekki prufað að keyra forritið í annarri tölvu, en grunar að það þurfi að gefa því leyfi með chmod.

Leikurinn sendir alltaf það sem er tapað á bottann, en þegar leikmaður vinnur þá verður að vera til UTXO sem er nógu stór til að senda upphæðina sem var löggð undir. Þetta er böggur og villumeðhöndlun sem ég hef ekki nægan tíma í að laga.

Bottinn sjálfur virkar þannig að þegar eitthvað er sent á hann, þá finnur byrjar hann á að finna út return address svo hann geti sent vinnin til baka á addressuna sem er að spila leikinn.
Svo reynir hann að finna OP_RETURN streng. Ef hann finnur hann þá ber hann það saman við "leyniorðið" sem '6a0199'. Sem sagt java forritið, leikurinn sjálfur sendir með strenginn "99" þegar leikmaður vinnur. Þá ber bottinn þetta saman og sér að það er vinningur og sendir til baka á leikmann. Þegar leikmaður tapar, þá einfaldlega sendir forritið upphæðina með `sendtoaddress` á bottann.

Ég átti töluvert erfitt með að finna út hvernig ég gæti fundið nógu stórt UTXO, en tókst að leysa það með því að ná í jq pakkann (https://stedolan.github.io/jq/) sem einfaldaði það að fara í gegnum öll UTXO í `listunspent`

Viðmótið er ekki flókið, en hefur allt sem þarf í þennan leik 21, og aðaláherslan var kannski líka bottinn og hvernig hann gæti fundið út hvað er vinningur og hvað ekki.

Ég myndi vilja klára alla villumeðhöndlun svo að leikmaður getur ekki spilað áfram ef hann á engin UTXO þó svo að hann eigi nógu marga broskalla, og mun örugglega gera það þrátt fyrir að það verði gert eftir skilafrest á þessu verkefni.

Það var mjög skemmtilegt að vinna þetta verkefni, þó svo að það hafi verið virkilega krefjandi á tímabilum þar sem ég hafði svo gott sem enga kunnáttu á skeljaskriftun og þurfti að kanna það svæði vel og lærði helling í leiðinni. Auðvitað eru einhverjir gallar, eins og til dæmis "leyniorðið". Það gæti í raun hver sem er tæmt reikninginn hjá bottanum mjög auðveldlega með því að skoða þennan kóða hér, en tilgangurinn með þessu verkefni hjá mér var ekki endilega að gera þetta 100% öruggt, heldur að fá virknina í lag.

Það væri gaman ef maður gæti einhvern tíman gert svona leiki og hostað á netinu, þar sem þessi er frekar bundinn bara við mína tölvu.
