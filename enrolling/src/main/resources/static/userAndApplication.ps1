

[CmdletBinding()] 

Param(
   [Parameter(Mandatory=$True,Position=1)]
   [string]$fname,
  
   [Parameter(Position=2)]
   [string]$lname ,

   [Parameter(Position=3)]
   [string]$email ,
 
   [Parameter(Position=4)]
   [string]$mobileNo
	) 



# To creating user

$fname = $fname.substring(0,1).ToUpper()+$fname.substring(1).ToLower() 
$lname = $lname.substring(0,1).ToUpper()+$lname.substring(1).ToLower()

$OUpath = "ou=trialspin,dc=opsmx,dc=com"
$displayname = $fname +" "+ $lname 
$username = ( $fname + $lname ).ToLower()
if ((Get-ADUser -F {mail -eq $email}) -And (Get-ADUser -F {mobilephone -eq $mobileNo}))
{
Write-Output '{'
            Write-Host '"userCreated": false,'
	        Write-Host '"userName":"'$Username'",'
	        Write-Host '"userPass":"'$Password1'",'
	        Write-Host '"description": "For user email and  Mobile number are duplicate"'

             Write-Host '}'
             exit

}


else
{

              # Auto password Generation 
                [reflection.assembly]::loadwithpartialname("system.web") | Out-Null
                $password1= [System.Web.Security.Membership]::GeneratePassword(12,1)
                # Write-Host '"userPass":''"' $Password1 '"'
              
              # New user creation if not in AD
              $userid
              New-ADUser -Name $username -UserPrincipalName $username@opsmx.com -Path $OUpath -AccountPassword (convertto-securestring $Password1 -AsPlainText -Force) -EA stop | Enable-ADAccount 
              
              # For setting user properties
              $User = Get-ADUser -Identity $username -Properties mail,displayname,mobilephone
              $User.mail = $email
			  $User.displayname = $displayname
			  $User.mobilephone = $mobileNo

			  Enable-ADAccount -Identity $username
			  Set-ADUser -Instance $User
			  
			  # To create group with user name
			  $groupid = "_grp"
			  $groupname = $username + $groupid
			  
			  # Creating new group
			  New-ADGroup $groupname -Path $OUpath -GroupCategory Security -GroupScope Global 
			  $adminuser = "CN=trialspin,OU=trialspin,DC=opsmx,DC=com"
			  Import-Module ActiveDirectory
			  
			  try {
					$GroupObj = Get-ADGroup -identity $groupname -EA stop
				  } 
				  
			  catch {
					# Write-Warning "$groupname : Group not found in Active Directory1"
					return
					}
				foreach($userid in $UserName) { 
					try {
						$userobj = Get-ADUser -identity $userid -EA 0 
						# Write-Host " The user in the group is $userid "
						if(!$userobj) { 
									# Write-Warning "$userid : This account is not found"
									continue
									}
						# Adding user to group

						Add-ADGroupMember -identity $GroupObj -Members $userobj,$adminuser -EA 0 
						
						$fileext = ".json"
						$filename = $gname + $fileext


						copy newap.json $filename
						(Get-Content .\$filename).replace('$inemail',$email) | Set-Content .\$filename
						(Get-Content .\$filename).Replace('$usname',$username) | Set-Content .\$filename
						(Get-Content .\$filename).Replace('$groupnam',$groupname) | Set-Content .\$filename

						spin app save --file $filename >$null 2>&1

						rm .\$filename

						# Succefully user and Group created. User added to created group
						Write-Output '{'
						Write-Host '"userCreated": true,'
						Write-Host '"userName":"'$Username'",'
						Write-Host '"userPass":"'$Password1'",'
						Write-Host '"description": "User with userid '$Username' created Successfully"'
						Write-Host	'}'
						exit
						} 
				catch { 
					# Write-Warning "$userid : Failed to add to the group"
					  }
				 } # For each
			     
           }
