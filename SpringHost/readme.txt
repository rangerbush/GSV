https://docs.aws.amazon.com/zh_cn/efs/latest/ug/mount-fs-auto-mount-onreboot.html
EFS在EC2启动时自动挂载

Glassfish 部署在efs上时，提示需要write权限：
sudo chmod -R 777 /mnt/efs

ubuntu@ip-172-31-40-16:/mnt/efs/glassfish5/glassfish/domains/domain1$ asadmin change-admin-password
Enter admin user name [default: admin]>
Enter the admin password>
Enter the new admin password>
Enter the new admin password again>
Command change-admin-password executed successfully.
回车,回车,密码,密码
asadmin --host 172.31.40.16 --port 4848 enable-secure-admin