https://docs.aws.amazon.com/zh_cn/efs/latest/ug/mount-fs-auto-mount-onreboot.html
EFS��EC2����ʱ�Զ�����

Glassfish ������efs��ʱ����ʾ��ҪwriteȨ�ޣ�
sudo chmod -R 777 /mnt/efs

ubuntu@ip-172-31-40-16:/mnt/efs/glassfish5/glassfish/domains/domain1$ asadmin change-admin-password
Enter admin user name [default: admin]>
Enter the admin password>
Enter the new admin password>
Enter the new admin password again>
Command change-admin-password executed successfully.
�س�,�س�,����,����
asadmin --host 172.31.40.16 --port 4848 enable-secure-admin