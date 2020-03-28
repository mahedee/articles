## How to contribute on an open source project

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
