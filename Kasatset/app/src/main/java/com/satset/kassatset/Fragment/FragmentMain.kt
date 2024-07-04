package com.satset.kassatset.Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.satset.kassatset.Fragment.FragmentPemasukan
import com.satset.kassatset.Fragment.FragmentPengeluaran
import com.satset.kassatset.R

class FragmentMain : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        tabLayout = findViewById(R.id.tab)
        viewPager = findViewById(R.id.viewpager)

        tabLayout.setupWithViewPager(viewPager)

        val vpAdapter = VPAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        vpAdapter.addFragment(FragmentPemasukan(), "Pemasukan")
        vpAdapter.addFragment(FragmentPengeluaran(), "Pengeluaran")
        viewPager.adapter = vpAdapter
    }
}
