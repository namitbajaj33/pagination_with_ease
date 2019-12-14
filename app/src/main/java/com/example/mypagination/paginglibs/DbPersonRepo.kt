package com.example.mypagination.paginglibs

import com.example.mypagination.Listing
import com.example.mypagination.db.InboxMsg

interface DbPersonRepo {

    fun getPersons(pageSize: Int): Listing<InboxMsg>

}