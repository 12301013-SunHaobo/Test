#!/usr/local/bin/python

import string, sys

resultStr = ('with lz_id {lz_id}+{lz_id} to outcomes {user}@{qstar_service_url}.  Count: {count}'.format(
        lz_id = 'lzid001',
        qstar_service_url = 'qurl001',
        user = 'user001',
        count = 'count001'
        ))

print resultStr