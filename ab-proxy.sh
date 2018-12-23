
echo "started"

ab -n 10 -c 1 localhost:5000/100 >      ab/with/proxy-n10c1-100.txt
ab -n 10 -c 5 localhost:5000/100 >      ab/with/proxy-n10c5-100.txt
ab -n 10 -c 10 localhost:5000/100 >     ab/with/proxy-n10c10-100.txt
ab -n 10 -kc 1 localhost:5000/100 >     ab/with/proxy-n10kc1-100.txt
ab -n 10 -kc 5 localhost:5000/100 >     ab/with/proxy-n10kc5-100.txt
ab -n 10 -kc 10 localhost:5000/100 >    ab/with/proxy-n10kc10-100.txt                                
ab -n 100 -c 1 localhost:5000/100 >     ab/with/proxy-n100c1-100.txt
ab -n 100 -c 5 localhost:5000/100 >     ab/with/proxy-n100c5-100.txt
ab -n 100 -c 10 localhost:5000/100 >    ab/with/proxy-n100c10-100.txt
ab -n 100 -kc 1 localhost:5000/100 >    ab/with/proxy-n100kc1-100.txt
ab -n 100 -kc 5 localhost:5000/100 >    ab/with/proxy-n100kc5-100.txt
ab -n 100 -kc 10 localhost:5000/100 >   ab/with/proxy-n100kc10-100.txt

echo "end of 100/100"   
echo "start 20000" 
ab -n 10 -c 1    localhost:5000/20000 > ab/with/proxy-n10c1-20000.txt
ab -n 10 -c 5    localhost:5000/20000 > ab/with/proxy-n10c5-20000.txt
ab -n 10 -c 10   localhost:5000/20000 > ab/with/proxy-n10c10-20000.txt
ab -n 10 -kc 1   localhost:5000/20000 > ab/with/proxy-n10kc1-20000.txt
ab -n 10 -kc 5   localhost:5000/20000 > ab/with/proxy-n10kc5-20000.txt
ab -n 10 -kc 10  localhost:5000/20000 > ab/with/proxy-n10kc10-20000.txt
ab -n 100 -c 1   localhost:5000/20000 > ab/with/proxy-n100c1-20000.txt
ab -n 100 -c 5   localhost:5000/20000 > ab/with/proxy-n100c5-20000.txt
ab -n 100 -c 10  localhost:5000/20000 > ab/with/proxy-n100c10-20000.txt
ab -n 100 -kc 1  localhost:5000/20000 > ab/with/proxy-n100kc1-20000.txt
ab -n 100 -kc 5  localhost:5000/20000 > ab/with/proxy-n100kc5-20000.txt
ab -n 100 -kc 10 localhost:5000/20000 > ab/with/proxy-n100kc10-20000.txt

                
echo "end"

                     
