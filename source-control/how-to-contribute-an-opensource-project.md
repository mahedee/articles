# How to contribute on an open source project

**Step 1: Fork the repository on which you want to contribute**  
For example, I have forked the following repository  
https://github.com/OpenCodeFoundation/eSchool

After forking my repository has became  
https://github.com/mahedee/eSchool

**Step 2: Clone your forked project**    
For example:  
```github
git clone https://github.com/mahedee/eSchool.git
```
So your remote repository is https://github.com/mahedee/eSchool.git  
if you want to see the remote repository type 

```github
git remote -v
```
output will be like 

```github
origin  https://github.com/mahedee/eSchool.git (fetch)
origin  https://github.com/mahedee/eSchool.git (push)
```
Here I see that the name of my remote repository is origin  

**Step 3: Add the original repository in the local repository**  
Now add the original repository as origin in the local repository and name as *upstream*  

```github
git remote add upstream https://github.com/OpenCodeFoundation/eSchool
```

So you have two remote link in your local repository  
1. origin - forked repository which is in your github  
2. upstream - original project's repository on which you will be contributed  

if you run the following command  

```github
git remote -v
```
you will see the following output  

```github
origin  https://github.com/mahedee/eSchool.git (fetch)
origin  https://github.com/mahedee/eSchool.git (push)
upstream        https://github.com/OpenCodeFoundation/eSchool (fetch)
upstream        https://github.com/OpenCodeFoundation/eSchool (push)
```

**Step 4: Open a new branch in my local repository**  

Before opening any branch or contributing, please run the following command to up to date your local repository  

```github
git pull upstream master
```

To check is there any new commit on upstream run-

```github
git status
```
you should see something like-  

```github
On branch master
Your branch is behind 'upstream/master' by 14 commits, and can be fast-forwarded.
  (use "git pull" to update your local branch)

nothing to commit, working tree clean
```
If you see, any changes in upstream, you can pull upstream to your local drive  

Now open a branch name *feature-joining* using following command

```github
git checkout -b feature-joining
```
Now you will see the 

```github
Mahedee@MahedeePC MINGW64 /d/Projects/Github/OpenCodeFoundation/eSchool (feature-joining)
```

instead of 

```github
Mahedee@MahedeePC MINGW64 /d/Projects/Github/OpenCodeFoundation/eSchool (master)
```

**Step 5: Work on the new branch and push**  
Now work on the new branch. After completing your task, just add, commit and push. Before that, you should check is there any change in the upstream. If change occur, just merge it with your local using the following command. 

```github
git pull --rebase upstream master
```
If you get any conflit, you have to resolve it locally. If no conflit occur, type the following command one by one.

```github
git add .
```

```github
git commit -m "complete feature-joining"
```

```github
git push origin feature-joining
```
**Step 6: Make a pull request**  
After completing the previous step, you will see something like the following image with a button *"Compare and pull request"* 

![alt text](https://github.com/mahedee/Articles/blob/master/img/git02.png)

Now press the button and make a pull request. Now reviewer can accept or reject the pull request (PR).

**Step 7: Add your change to your forked repository**
Now, if you want to add your change to your forked repository. Just type the following command. Remember, in this case, your forked repository and original repository may not sync if the PR is not accepted. 

```github
git checkout master 
git merge feature-joining 
git push origin master
```
**Step 8: Sync forked repository with original repository**  
If you want to sync your forked repository with original repository at any time. Tyep the following command.

```github
git pull upstream master 
git push origin master
```

**Step 9: Delete the branch**
After accepting or rejecting your PR. You should remove the branch. For new work, you should create a new branch. To remove the local branch type the following command  
```github
git branch -d feature-joining
```
If you want to remove this branch from github then type the following command.
```github
git push origin :feature-joining 
```
Thank you for your patience :) 
