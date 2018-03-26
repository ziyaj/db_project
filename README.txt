A repo for a project in a database course

Git command workflow:
# first check what changes I have made in my repo
git status

# stash the changes 
git stash save

# use git status to check again, your repo should be clean now
# because all the local changes are put into stash
git status

# pull the newest code, a clean repo should result in no merge conflict
git pull

# pop the stashed changes
git stash pop

# now a merge conflict can happen
vim App.java # remove the weird symbols and leave the code you want

git add App.java # if all weird symbols are remvoed, the add should be successful

# use git status again to check the current status
git status # you should see that App.java is green now

# commit the merged file back to repo
git commit

# push the commited changes
git push


