echo "updaing server" >> log.txt
git pull >> log.txt
echo "downloading files" >> log.txt
forever restartall >> log.txt
echo "started server" >> log.txt
